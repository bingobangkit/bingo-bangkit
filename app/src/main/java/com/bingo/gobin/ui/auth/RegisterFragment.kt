package com.bingo.gobin.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bingo.gobin.R
import com.bingo.gobin.data.model.User
import com.bingo.gobin.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {
    private val binding : FragmentRegisterBinding by viewBinding()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        binding.btnRegisterPage.setOnClickListener {
            register()
        }
    }

    fun register(){
        val name = binding.fieldName.editText?.text.toString()
        val email = binding.fieldEmail.editText?.text.toString()
        val phone = binding.fieldPhone.editText?.text.toString()
        val password = binding.fieldPassword.editText?.text.toString()
        val data = User(name, email, phone, password)
    }

}