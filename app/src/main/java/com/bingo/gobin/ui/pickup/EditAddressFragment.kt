package com.bingo.gobin.ui.pickup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bingo.gobin.R
import com.bingo.gobin.databinding.FragmentEditAddressBinding


class EditAddressFragment : Fragment() {
    private val binding : FragmentEditAddressBinding by viewBinding()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_address, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnPickLocation.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.container_map, MapsFragment())
                addToBackStack(null)
            }
        }
    }
}