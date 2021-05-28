package com.bingo.gobin.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bingo.gobin.R
import com.bingo.gobin.databinding.FragmentDetailPickupBinding
import com.bingo.gobin.util.ID_USER_SEMENTARA
import kotlinx.coroutines.launch


class DetailPickupFragment : Fragment() {

    companion object {
        const val ID = ""
    }

    private lateinit var binding: FragmentDetailPickupBinding
    private val detailViewModel: DetailViewModel by viewModels()
    private var saldo:Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailPickupBinding.inflate(layoutInflater)

        binding.btnBack.setOnClickListener{parentFragmentManager.popBackStack()}

        val id = arguments?.getString(ID)
        id?.let { getDetailOrder(it) }

        return binding.root
    }

    private fun getDetailOrder(id: String) {
        lifecycleScope.launch {
            detailViewModel.getDetailOrder(id).observe(viewLifecycleOwner, { value ->

                if (value.status == "complete") {
                    binding.txtDetailStatus.setTextColor(
                        resources.getColor(R.color.green)
                    )
                }

                binding.apply {
                    txtAddress.text = value.address
                    txtDetailStatus.text = "Order " + value.status
                    txtDetailDate.text = value.date
                    txtDetailInvoice.text = value.id_invoice
                    txtDetailAmountPlasticBottle.text = value.amount + " Kg"
                    txtDetailTotalAmount.text = "Rp. " + value.total_price
                    txtDetailPriceBottleOrderInfo.text = "Rp. " + value.total_price
                    txtDetailTotalIncome.text =
                        "Rp. " + (value.total_price?.toInt()?.minus(2000)).toString()
                    txtGetpay.text = "Rp. " + (value.total_price?.toInt()?.minus(2000)).toString()



                }
                getUserById(ID_USER_SEMENTARA, value.total_price?.toInt()?.minus(2000)!!)

            })
        }
    }

    fun getUserById(id: String,balance: Int){
        lifecycleScope.launch {
            detailViewModel.getUserById(id).observe(viewLifecycleOwner,{user->
                this@DetailPickupFragment.saldo = user.saldo?.toInt()!!
                Log.d("detail",this@DetailPickupFragment.saldo.toString())
                Log.d("detail",user.saldo)
//                updateBalance(id, (this@DetailPickupFragment.saldo+balance).toString())
            })
        }
    }

//    fun updateBalance(id: String,balance:String){
//
//            detailViewModel.updateBalance(id,balance)
//
//    }

}