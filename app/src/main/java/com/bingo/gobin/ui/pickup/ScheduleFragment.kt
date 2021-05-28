package com.bingo.gobin.ui.pickup

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bingo.gobin.R
import com.bingo.gobin.data.model.Order
import com.bingo.gobin.databinding.FragmentScheduleBinding
import com.bingo.gobin.ui.detail.DetailPickupFragment
import com.bingo.gobin.util.ID_USER_SEMENTARA
import kotlinx.coroutines.launch


class ScheduleFragment : Fragment() {
    private val viewModel: ScheduleViewModel by activityViewModels()
    private val binding: FragmentScheduleBinding by viewBinding()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        getUser()
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnSchedule.setOnClickListener {
                parentFragmentManager.commit {
                    replace(R.id.main_fragment_container, PickUpFragment())
                    addToBackStack(null)
                }
            }
        }


        getListSchedule()


    }

    private fun getUser(){
        lifecycleScope.launch {
            viewModel.getUserById(ID_USER_SEMENTARA).observe(viewLifecycleOwner,{user->
                binding.apply {
                    totalPickup.text = user.jml_pickup
                    walletBalance.text = user.saldo
                    pointEarned.text = user.poin
                }
            })
        }
    }

    private fun getListSchedule() {
        with(binding) {
            lifecycleScope.launchWhenStarted {
                viewModel.getUserOrder(ID_USER_SEMENTARA)
                    .observe(viewLifecycleOwner, {
                        Log.d("TAG", "getListSchedule: $it")
//                        if (it.isNotEmpty()) {
//                            uiWaiting()
//                        } else {
//                            uiNoSchedule()
//                        }
                        rvSchedule.apply {
                            layoutManager =
                                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                            val listOrder = ScheduleAdapter(it)
                            listOrder.setOnItemClickCallback(object : ScheduleAdapter.OnItemClickCallback {
                                override fun onItemClicked(order: Order) {
                                    val bundle = Bundle()
                                    Log.d("main","id"+order.id)
                                    bundle.putString(DetailPickupFragment.ID, order.id)
                                    val detailPickupFragment = DetailPickupFragment()
                                    detailPickupFragment.arguments = bundle
                                    parentFragmentManager.beginTransaction()
                                        .replace(R.id.main_fragment_container, detailPickupFragment).addToBackStack(null)
                                        .commit()
                                }
                            })
                            adapter = listOrder

                        }
                    })
            }
        }
    }

//    private fun cekAja() {
//        lifecycleScope.launch {
//            viewModel.getUserOrder("ZS91wKaGmkdMvCTZ4Ko6B8EkUw52","waiting").observe(viewLifecycleOwner,{
//                Log.d("TAG", "cekAja: $it")
//            })
//        }
//    }
//
//    fun uiNoSchedule() {
//        with(binding) {
//            upcomingTitle.visibility = View.GONE
//            rvSchedule.visibility = View.GONE
//            liveTrackTitle.visibility = View.GONE
//            containerMap.visibility = View.GONE
//            cardArrivingTrack.visibility = View.GONE
//            imgWaiting.visibility = View.GONE
//            upcomingTitle.visibility = View.GONE
//            txtArriving.visibility = View.GONE
//            txtWaiting.visibility = View.GONE
//            btnTrack.visibility = View.GONE
//            txtNoSchedule.visibility = View.VISIBLE
//            imgNoSchedule.visibility = View.VISIBLE
//        }
//    }
//
//    fun uiWaiting() {
//        with(binding) {
//            upcomingTitle.visibility = View.VISIBLE
//            rvSchedule.visibility = View.VISIBLE
//            liveTrackTitle.visibility = View.GONE
//            containerMap.visibility = View.GONE
//            cardArrivingTrack.visibility = View.GONE
//            imgWaiting.visibility = View.VISIBLE
//            upcomingTitle.visibility = View.VISIBLE
//            txtArriving.visibility = View.GONE
//            txtWaiting.visibility = View.VISIBLE
//            btnTrack.visibility = View.GONE
//            txtNoSchedule.visibility = View.GONE
//            imgNoSchedule.visibility = View.GONE
//        }
//    }
//
//    fun uiArriving() {
//        with(binding) {
//            upcomingTitle.visibility = View.VISIBLE
//            rvSchedule.visibility = View.VISIBLE
//            liveTrackTitle.visibility = View.VISIBLE
//            containerMap.visibility = View.VISIBLE
//            cardArrivingTrack.visibility = View.VISIBLE
//            imgWaiting.visibility = View.GONE
//            upcomingTitle.visibility = View.VISIBLE
//            txtArriving.visibility = View.VISIBLE
//            txtWaiting.visibility = View.GONE
//            btnTrack.visibility = View.VISIBLE
//            txtNoSchedule.visibility = View.GONE
//            imgNoSchedule.visibility = View.GONE
//        }
//    }


}