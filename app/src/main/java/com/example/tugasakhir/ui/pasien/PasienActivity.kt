package com.example.tugasakhir.ui.pasien

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.tugasakhir.data.api.RetrofitClient
import com.example.tugasakhir.data.local.TokenManager
import com.example.tugasakhir.databinding.ActivityPasienBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PasienActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPasienBinding
    private lateinit var tokenManager: TokenManager
    private val adapter = PasienAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasienBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tokenManager = TokenManager(this)

        val userName = tokenManager.getUserName()
        binding.tvGreeting.text = if (!userName.isNullOrEmpty()) {
            "Selamat datang, $userName"
        } else {
            "Selamat datang"
        }

        binding.rvPasien.adapter = adapter

        loadPasien()
    }

    private fun loadPasien() {
        setLoading(true)
        binding.tvEmpty.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.apiService.getPasien()
                }

                if (response.isSuccessful) {
                    val pasienResponse = response.body()
                    if (pasienResponse != null && pasienResponse.success) {
                        val data = pasienResponse.data
                        binding.tvInfo.text = "Menampilkan ${data.size} data pasien"
                        if (data.isEmpty()) {
                            binding.tvEmpty.visibility = View.VISIBLE
                        } else {
                            adapter.submitList(data)
                        }
                    } else {
                        binding.tvInfo.text = pasienResponse?.message ?: "Gagal memuat data"
                    }
                } else {
                    if (response.code() == 401) {
                        binding.tvInfo.text = "Sesi berakhir, silakan login kembali"
                    } else {
                        binding.tvInfo.text = "Gagal memuat data (${response.code()})"
                    }
                }
            } catch (e: Exception) {
                binding.tvInfo.text = "Terjadi kesalahan: ${e.localizedMessage ?: "Koneksi gagal"}"
            } finally {
                setLoading(false)
            }
        }
    }

    private fun setLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        binding.rvPasien.visibility = if (loading) View.GONE else View.VISIBLE
    }
}
