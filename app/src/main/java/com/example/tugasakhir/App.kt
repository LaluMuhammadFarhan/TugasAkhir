package com.example.tugasakhir

import android.app.Application
import com.example.tugasakhir.data.api.RetrofitClient
import com.example.tugasakhir.data.local.TokenManager

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        val tokenManager = TokenManager(this)
        RetrofitClient.init(tokenManager)
    }
}
