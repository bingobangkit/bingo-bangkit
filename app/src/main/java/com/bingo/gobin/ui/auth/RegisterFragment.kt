package com.bingo.gobin.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bingo.gobin.R
import com.bingo.gobin.data.model.User
import com.bingo.gobin.databinding.FragmentRegisterBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
class RegisterFragment : Fragment() {
    private val viewModel: AuthViewModel by viewModels()
    private val binding: FragmentRegisterBinding by viewBinding()
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

    private fun register() {
        val name = binding.fieldName.editText?.text.toString()
        val email = binding.fieldEmail.editText?.text.toString()
        val phone = binding.fieldPhone.editText?.text.toString()
        val password = binding.fieldPassword.editText?.text.toString()
        val data = User(name = name, phone = phone)

        if (name.isBlank() || email.isBlank() || phone.isBlank() || password.isBlank()){
            Toast.makeText(context, "Cek kembali form Anda!", Toast.LENGTH_SHORT).show()
            return
        }else{
            viewModel.register(user = data, email = email, password = password)
                .observe(viewLifecycleOwner, {
                    if (it) {
                        Toast.makeText(context, "Berhasil Register, silakan login!", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(context, "Gagal Register", Toast.LENGTH_SHORT).show()
                    }
                })
        }

    }


}