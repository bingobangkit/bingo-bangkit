package com.bingo.gobin.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bingo.gobin.R
import com.bingo.gobin.databinding.ActivityAuthBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi

class AuthActivity : AppCompatActivity() {
    private val viewModel: AuthViewModel by viewModels()
    private val binding: ActivityAuthBinding by viewBinding()
    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        with(binding) {
            btnRegister.setOnClickListener {

                val intent = Intent(this@AuthActivity, RegisterActivity::class.java)
                startActivity(intent)

            }
            btnLogin.setOnClickListener {
                login()
            }

        }
    }
    private fun login() {
        with(binding) {
            val email = fieldEmailLogin.text.toString()
            val password = fieldPassword.text.toString()
            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this@AuthActivity, "Field tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.login(email, password).observe(this@AuthActivity, {
                    if (it) {
                        Toast.makeText(this@AuthActivity, "Berhasil Login", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@AuthActivity, "Error", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }
}