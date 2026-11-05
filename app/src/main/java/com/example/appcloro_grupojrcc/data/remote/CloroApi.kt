package com.example.appcloro_grupojrcc.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CloroApi {

    // Mediciones posteriores a la fecha indicada (0 = todas)
    @GET("api/mediciones")
    suspend fun mediciones(@Query("desde") desde: Long): List<MedicionRemota>

    @POST("api/mediciones")
    suspend fun subir(@Body nuevas: List<NuevaMedicionRemota>): List<MedicionRemota>
}