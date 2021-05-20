package com.bingo.gobin.ui.pickup

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bingo.gobin.R
import com.bingo.gobin.databinding.FragmentPickUpBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class PickUpFragment : Fragment() {
    private val viewModel: PickupViewModel by viewModels()
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

    }

    @SuppressLint("SimpleDateFormat", "ResourceAsColor")
    private fun pickDate() {
        DatePickerDialog().accentColor = R.color.navy
        DatePickerDialog.newInstance { _, year, monthOfYear, dayOfMonth ->
            val cal = Calendar.getInstance()
            cal.set(Calendar.YEAR,year)
            cal.set(Calendar.MONTH,monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH,dayOfMonth)
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

    fun setAddress() {

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


}