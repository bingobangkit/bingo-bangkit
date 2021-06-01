package com.bingo.gobin.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.bingo.gobin.R
import com.bingo.gobin.data.content.DataContent
import com.bingo.gobin.databinding.FragmentContentBinding
import com.bingo.gobin.ui.ProfileActivity
import com.bingo.gobin.ui.auth.AuthActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


@Suppress("DEPRECATION")
class ContentFragment : Fragment() {
    private var _binding: FragmentContentBinding? = null
    private val binding get() = _binding!!
    private val mAdapter = ContentAdapter()
    private val common = DataContent.getContent()[0]
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            binding.imageView.setOnClickListener {
                val auth = Firebase.auth.currentUser
                if (auth == null){
                    val intent = Intent(context,AuthActivity::class.java)
                    startActivity(intent)
                }else{
                    val intent = Intent(context,ProfileActivity::class.java)
                    startActivity(intent)
                }

            }
            radioGreen.isChecked = true
            animasikan(radioGreen, true)
            rvContent.apply {
                val co = DataContent.getContent().find { it.name == "PETE" }
                adapter = ContentAdapter(co!!.commonUses)
                layoutManager = GridLayoutManager(context, 2)
                setHasFixedSize(true)
                recycleType.text = common.type
                recycleDesc.text = common.description
            }


            radioGreen.setOnCheckedChangeListener { buttonView, isChecked ->
                animasikan(buttonView, isChecked)
                val co = DataContent.getContent().find { it.name == "PETE" }
                if (isChecked) {
                    mAdapter.list = co?.commonUses ?: emptyList()
                    rvContent.adapter = mAdapter
                    recycleType.text = co!!.type
                    recycleDesc.text = co.description
                    materialCardView2.setCardBackgroundColor(resources.getColor(R.color.green))
                }

            }
            radioBlue.setOnCheckedChangeListener { buttonView, isChecked ->
                animasikan(buttonView, isChecked)
                val co = DataContent.getContent().find { it.name == "HDPE" }
                if (isChecked) {
                    mAdapter.list = co?.commonUses ?: emptyList()
                    rvContent.adapter = mAdapter
                    recycleType.text = co!!.type
                    recycleDesc.text = co.description
                    materialCardView2.setCardBackgroundColor(resources.getColor(R.color.green))
                }
            }
            radioYellow.setOnCheckedChangeListener { buttonView, isChecked ->
                animasikan(buttonView, isChecked)
                val co = DataContent.getContent().find { it.name == "PVC" }
                if (isChecked) {
                    mAdapter.list = co?.commonUses ?: emptyList()
                    rvContent.adapter = mAdapter
                    recycleType.text = co!!.type
                    recycleDesc.text = co.description
                    materialCardView2.setCardBackgroundColor(resources.getColor(R.color.yellow))
                    materialCardView2.scaleY = 1.25f
                }else{
                    materialCardView2.scaleY = 1f
                }
            }
            radioRed.setOnCheckedChangeListener { buttonView, isChecked ->
                animasikan(buttonView, isChecked)
                val co = DataContent.getContent().find { it.name == "LDPE" }
                if (isChecked) {
                    mAdapter.list = co?.commonUses ?: emptyList()
                    rvContent.adapter = mAdapter
                    recycleType.text = co!!.type
                    recycleDesc.text = co.description
                    materialCardView2.setCardBackgroundColor(resources.getColor(R.color.red))
                }
            }


        }


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun animasikan(view: View, state: Boolean) {
        if (state) {
            view.animate()
                .scaleX(1.2f)
                .scaleY(1.2f)
                .setInterpolator(BounceInterpolator())
                .setDuration(500L)
                .start()
        } else {
            view.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setInterpolator(BounceInterpolator())
                .setDuration(300L)
                .start()
        }

    }
}