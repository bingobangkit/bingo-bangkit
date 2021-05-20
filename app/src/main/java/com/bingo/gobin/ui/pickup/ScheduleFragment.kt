package com.bingo.gobin.ui.pickup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bingo.gobin.R
import com.bingo.gobin.databinding.FragmentScheduleBinding


class ScheduleFragment : Fragment() {

    private val binding : FragmentScheduleBinding by viewBinding()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            btnSchedule.setOnClickListener {
                parentFragmentManager.commit {
                    replace(R.id.main_fragment_container,PickUpFragment())
                    addToBackStack(null)
                }
            }
        }
    }

    fun uiNoSchedule(){
        with(binding){
            upcomingTitle.visibility = View.GONE
            rvSchedule.visibility = View.GONE
            liveTrackTitle.visibility = View.GONE
            containerMap.visibility = View.GONE
            cardArrivingTrack.visibility = View.GONE
            imgWaiting.visibility = View.GONE
            upcomingTitle.visibility = View.GONE
            txtArriving.visibility = View.GONE
            txtWaiting.visibility = View.GONE
            btnTrack.visibility = View.GONE
            txtNoSchedule.visibility = View.VISIBLE
            imgNoSchedule.visibility = View.VISIBLE
        }
    }

    fun uiWaiting(){
        with(binding){
            upcomingTitle.visibility = View.VISIBLE
            rvSchedule.visibility = View.VISIBLE
            liveTrackTitle.visibility = View.GONE
            containerMap.visibility = View.GONE
            cardArrivingTrack.visibility = View.GONE
            imgWaiting.visibility = View.VISIBLE
            upcomingTitle.visibility = View.VISIBLE
            txtArriving.visibility = View.GONE
            txtWaiting.visibility = View.VISIBLE
            btnTrack.visibility = View.GONE
            txtNoSchedule.visibility = View.GONE
            imgNoSchedule.visibility = View.GONE
        }
    }

    fun uiArriving(){
        with(binding){
            upcomingTitle.visibility = View.VISIBLE
            rvSchedule.visibility = View.VISIBLE
            liveTrackTitle.visibility = View.VISIBLE
            containerMap.visibility = View.VISIBLE
            cardArrivingTrack.visibility = View.VISIBLE
            imgWaiting.visibility = View.GONE
            upcomingTitle.visibility = View.VISIBLE
            txtArriving.visibility = View.VISIBLE
            txtWaiting.visibility = View.GONE
            btnTrack.visibility = View.VISIBLE
            txtNoSchedule.visibility = View.GONE
            imgNoSchedule.visibility = View.GONE
        }
    }


}