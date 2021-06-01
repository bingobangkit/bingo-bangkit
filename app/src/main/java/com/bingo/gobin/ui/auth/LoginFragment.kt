package com.bingo.gobin.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bingo.gobin.R
import com.bingo.gobin.databinding.FragmentLoginBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class LoginFragment : Fragment() {

    private val viewModel: AuthViewModel by viewModels()
    private val binding: FragmentLoginBinding by viewBinding()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnRegister.setOnClickListener {
                parentFragmentManager.commit {
                    replace(R.id.fragmentContainerView, RegisterFragment())
                }
            }
            btnLogin.setOnClickListener {
//                login()
            }

        }
    }

//    private fun login() {
//        with(binding) {
//            val email = fieldEmailLogin.text.toString()
//            val password = fieldPassword.text.toString()
//            if (email.isBlank() || password.isBlank()) {
//                Toast.makeText(context, "Field tidak boleh kosong", Toast.LENGTH_SHORT).show()
//            } else {
//                viewModel.login(email, password).observe(viewLifecycleOwner, {
//                    if (it) {
//                        Toast.makeText(context, "Berhasil Login", Toast.LENGTH_SHORT).show()
//                        activity?.finish()
//                    } else {
//                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
//                    }
//                })
//            }
//        }
//    }


}