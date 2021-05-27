package com.bingo.gobin.ui.pickup

import android.annotation.SuppressLint
import android.os.Bundle
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
import com.bingo.gobin.databinding.FragmentPickUpBinding
import com.bingo.gobin.util.ID_USER_SEMENTARA
import com.bingo.gobin.util.INITIAL_ID_TYPE
import com.bingo.gobin.util.INITIAL_STATUS_ORDER
import com.bingo.gobin.util.INITIAL_TYPE_INVOICE
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class PickUpFragment : Fragment() {
    private val viewModel: ScheduleViewModel by activityViewModels()
    private val binding: FragmentPickUpBinding by viewBinding()

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
        setOrderInfo()
        getCurrentDate()
        viewModel.address.observe(viewLifecycleOwner, {
            binding.txtAddress.text = it
        })
        viewModel.amountPlastic.observe(viewLifecycleOwner, {
            val data = it.toString()
            binding.txtKgRecyle.text = "${data}kg"
        })
        viewModel.getTotal().observe(viewLifecycleOwner, {
            binding.txtTotal.text = it.toString()
        })
        lifecycleScope.launch {
            viewModel.getType().let {
                binding.txtPricePlastic.text = "(Rp.${it[0].price}/kg)"
            }
        }

        binding.btnCardDate.setOnClickListener { pickDate() }
        binding.txtDate.setOnClickListener { pickDate() }
        binding.btnCardAddress.setOnClickListener {
            parentFragmentManager.commit {
                add(R.id.main_fragment_container, EditAddressFragment())
                addToBackStack(null)
            }
        }
        binding.btnSchedulePickup.setOnClickListener {
            if(schedulePickup()){
                parentFragmentManager.popBackStack()
            }else{
                Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show()
            }
        }
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
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.VISIBLE
    }

    private fun schedulePickup() : Boolean {
        var sch_status : Boolean  = false
        lifecycleScope.launchWhenStarted {
            with(viewModel){
                val date = binding.txtDate.text.toString()
                val id_type = INITIAL_ID_TYPE    //viewModel.getType().find { it.id == "1" }?.id
                val amount = amountPlastic.value.toString()
                val total_price = binding.txtTotal.text.toString()
                val id_invoice = DateUtil.generateInvoice(INITIAL_TYPE_INVOICE)
                val address = this.address.value
                val latitude = this.latitude.value
                val longitude = this.longitude.value
                var status = INITIAL_STATUS_ORDER

                val order = Order(
                    id_invoice = id_invoice,
                    id_type = id_type,
                    address = address,
                    amount = amount,
                    latitude = latitude,
                    longitude = longitude,
                    status = status,
                    date = date,
                    id_user = ID_USER_SEMENTARA ,
                    total_price = total_price,

                    )
                if(setOrder(order)){
                    sch_status = true
                }
            }

        }
        return sch_status
    }


}