package com.bingo.gobin.ui.pickup

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bingo.gobin.DateUtil
import com.bingo.gobin.R
import com.bingo.gobin.data.model.Order
import com.bingo.gobin.data.model.User
import com.bingo.gobin.databinding.FragmentPickUpBinding
import com.bingo.gobin.util.ID_USER_SEMENTARA
import com.bingo.gobin.util.INITIAL_ID_TYPE
import com.bingo.gobin.util.INITIAL_STATUS_ORDER
import com.bingo.gobin.util.INITIAL_TYPE_INVOICE
import com.fondesa.kpermissions.PermissionStatus
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.anyPermanentlyDenied
import com.fondesa.kpermissions.anyShouldShowRationale
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.request.PermissionRequest
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mapbox.android.core.location.LocationEngineCallback
import com.mapbox.android.core.location.LocationEngineResult
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.squareup.okhttp.Dispatcher
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SetTextI18n")
class PickUpFragment : Fragment(), PermissionRequest.Listener {
    private val request by lazy {
        permissionsBuilder(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ).build()
    }
    private val viewModel: ScheduleViewModel by activityViewModels()
    private val binding: FragmentPickUpBinding by viewBinding()
    private var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        return inflater.inflate(R.layout.fragment_pick_up, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.GONE
        request.send()
        setOrderInfo()
        getCurrentDate()
        initialAddress()
        setAmount()
        setTotal()
        setPrice()
        setSchedule()


    }

    private fun initialAddress() {
        lifecycleScope.launchWhenStarted {
            user = getUserData()
            binding.txtAddress.text = withContext(Dispatchers.Main) {
                user?.address
            }
        }

    }

    private fun setSchedule() {
        viewModel.address.observe(viewLifecycleOwner, {
            binding.txtAddress.text = it
        })
        binding.btnCardDate.setOnClickListener { pickDate() }
        binding.txtDate.setOnClickListener { pickDate() }
        binding.btnCardAddress.setOnClickListener {
            parentFragmentManager.commit {
                add(R.id.main_fragment_container, EditAddressFragment())
                addToBackStack(null)
            }
        }
        binding.btnSchedulePickup.setOnClickListener {
            if (schedulePickup()) {
                parentFragmentManager.popBackStack()
            } else {
                Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun setPrice() {
        lifecycleScope.launch {
            viewModel.getType().let {
                binding.txtPricePlastic.text = "(Rp.${it[0].price}/kg)"
                binding.txtPriceCard.text = "(Rp.${it[1].price}/kg)"
                binding.txtPriceSteel.text = "(Rp.${it[2].price}/kg)"
            }
        }
    }

    private fun setTotal() {
        viewModel.totalPlastic.observe(viewLifecycleOwner, {
            binding.txtTotalPlastic.text = it.toString()
        })

        viewModel.totalCard.observe(viewLifecycleOwner, {
            binding.txtTotalCardboard.text = it.toString()
        })
        viewModel.totalSteel.observe(viewLifecycleOwner, {
            binding.txtTotalSteel.text = it.toString()
        })




        viewModel.getTotal().observe(viewLifecycleOwner, {
            binding.txtTotal.text = it.toString()
        })
    }

    private fun setAmount() {
        viewModel.amountPlastic.observe(viewLifecycleOwner, {
            val data = it.toString()
            binding.txtKgRecyle.text = "${data}kg"
        })
        viewModel.amountCardboard.observe(viewLifecycleOwner, {
            val data = it.toString()
            binding.txtKgCard.text = "${data}kg"
        })

        viewModel.amountSteel.observe(viewLifecycleOwner, {
            val data = it.toString()
            binding.txtKgSteel.text = "${data}kg"
        })
    }

    @SuppressLint("SimpleDateFormat", "ResourceAsColor")
    private fun pickDate() {
        DatePickerDialog.newInstance { view, year, monthOfYear, dayOfMonth ->
            val cal = Calendar.getInstance()
            view.minDate = cal
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val format = SimpleDateFormat("dd MMMM, yyyy").format(cal.time)
            viewModel.setDatePickup(format)
        }.show(parentFragmentManager, "Datepickerdialog")
    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDate() {
        val cal = Calendar.getInstance()
        val format = SimpleDateFormat("dd MMMM, yyyy").format(cal.time)
        viewModel.setDatePickup(format)
        viewModel.datePickup.observe(viewLifecycleOwner, {
            binding.txtDate.text = it
        })
    }


    private fun setOrderInfo() {
        with(binding) {
            btnBack.setOnClickListener { requireActivity().onBackPressed() }

            btnPlusType.setOnClickListener { viewModel.addPlasticAmount() }
            btnMinType.setOnClickListener { viewModel.minPlasticAmount() }

            btnPlusCard.setOnClickListener { viewModel.addCardboardAmount() }
            btnMinCard.setOnClickListener { viewModel.minCardboardAmount() }

            btnPlusSteel.setOnClickListener { viewModel.addSteelAmount() }
            btnMinSteel.setOnClickListener { viewModel.minSteelAmount() }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.VISIBLE
    }

    private fun schedulePickup(): Boolean {
        var sch_status = false
        lifecycleScope.launchWhenStarted {
            with(viewModel) {
                val date = binding.txtDate.text.toString()
                val id_type = INITIAL_ID_TYPE    //viewModel.getType().find { it.id == "1" }?.id
                val amount_plastic = amountPlastic.value.toString()
                val amount_cardboard = amountCardboard.value.toString()
                val amount_steel = amountSteel.value.toString()
                val amount =
                    (amount_plastic.toInt() + amount_cardboard.toInt() + amount_steel.toInt()).toString()
                val total_price = binding.txtTotal.text.toString()
                val total_price_plastic = binding.txtTotalPlastic.text.toString()
                val total_price_cardboard = binding.txtTotalCardboard.text.toString()
                val total_price_steel = binding.txtTotalSteel.text.toString()
                val id_invoice = DateUtil.generateInvoice(INITIAL_TYPE_INVOICE)
                val address =  user?.address ?: this.address.value
                val latitude =  user?.latitude ?: this.latitude.value
                val longitude = user?.longitude ?: this.longitude.value
                val status = INITIAL_STATUS_ORDER
                if (amount=="0"){
                    sch_status = false
                    return@launchWhenStarted
                }
                val order = Order(
                    id_invoice = id_invoice,
                    id_type = id_type,
                    address = address,
                    amount = amount,
                    amount_plastic = amount_plastic,
                    amount_cardboard = amount_cardboard,
                    amount_steel = amount_steel,
                    latitude = latitude,
                    longitude = longitude,
                    status = status,
                    date = date,
                    id_user = user?.id ?: ID_USER_SEMENTARA,
                    total_price = total_price,
                    total_cardboard = total_price_cardboard,
                    total_plastic = total_price_plastic,
                    total_steel = total_price_steel,

                    )
                if (setOrder(order)) sch_status = true
            }

        }
        return sch_status
    }

    override fun onPermissionsResult(result: List<PermissionStatus>) {
        val context = requireContext().applicationContext
        when {
            result.anyPermanentlyDenied() -> Toast.makeText(
                context,
                "Harus menambah permission",
                Toast.LENGTH_SHORT
            ).show()
            result.anyShouldShowRationale() -> Toast.makeText(
                context,
                "Harus menambah permission",
                Toast.LENGTH_SHORT
            ).show()
            result.allGranted() -> return
        }
    }

    suspend fun getUserData(): User? {
        val uid = Firebase.auth.currentUser?.uid
        if (uid != null) {
            return Firebase.firestore.collection("users")
                .whereEqualTo("id", uid)
                .get().await().toObjects(User::class.java)[0]
        } else {
            return null
        }
    }

}