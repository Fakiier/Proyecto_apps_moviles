package com.example.appcloro_grupojrcc.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ClienteApi {

    // Emulador: 10.0.2.2 es tu PC. En un celular real, usa la IP de tu PC en la misma red Wi-Fi.
    const val URL_BASE = "http://192.168.100.7:8098/"

    val api: CloroApi by lazy {
        val cliente = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
        Retrofit.Builder()
            .baseUrl(URL_BASE)
            .client(cliente)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CloroApi::class.java)
    }
}