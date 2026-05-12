package com.example.tugasakhir.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tugasakhir.data.api.RetrofitClient
import com.example.tugasakhir.data.local.TokenManager
import com.example.tugasakhir.databinding.ActivityLoginBinding
import com.example.tugasakhir.ui.pasien.PasienActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tokenManager = TokenManager(this)

        binding.btnLogin.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        binding.tvError.visibility = View.GONE

        if (email.isEmpty()) {
            binding.tilEmail.error = "Email tidak boleh kosong"
            binding.etEmail.requestFocus()
            return
        } else {
            binding.tilEmail.error = null
        }

        if (password.isEmpty()) {
            binding.tilPassword.error = "Password tidak boleh kosong"
            binding.etPassword.requestFocus()
            return
        } else {
            binding.tilPassword.error = null
        }

        setLoading(true)

        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.apiService.login(email, password)
                }

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null && loginResponse.success) {
                        val token = loginResponse.data?.token
                        val userName = loginResponse.data?.user?.name

                        if (!token.isNullOrEmpty()) {
                            tokenManager.saveToken(token)
                        }
                        if (!userName.isNullOrEmpty()) {
                            tokenManager.saveUserName(userName)
                        }

                        val intent = Intent(this@LoginActivity, PasienActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        showError(loginResponse?.message ?: "Login gagal")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    showError(errorBody ?: "Login gagal, periksa kembali email dan password")
                }
            } catch (e: Exception) {
                showError("Terjadi kesalahan: ${e.localizedMessage ?: "Koneksi gagal"}")
            } finally {
                setLoading(false)
            }
        }
    }

    private fun setLoading(loading: Boolean) {
        if (loading) {
            binding.btnLogin.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
            binding.etEmail.isEnabled = false
            binding.etPassword.isEnabled = false
        } else {
            binding.btnLogin.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
            binding.etEmail.isEnabled = true
            binding.etPassword.isEnabled = true
        }
    }

    private fun showError(message: String) {
        binding.tvError.text = message
        binding.tvError.visibility = View.VISIBLE
    }
}
