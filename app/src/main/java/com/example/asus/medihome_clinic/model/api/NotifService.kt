package com.example.asus.medihome_clinic.model.api

import com.example.asus.medihome_clinic.util.Constant
import retrofit2.Call
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotifService{

    @Headers( "Content-Type: application/json" )
    @POST("send")
    fun sendNotifToClinic(@Body notif : NotifReservasi, @Header("Authorization") key : String) : Call<NotifResponse>

    companion object Factory {
        fun create(): NotifService {
            val retrofit = retrofit2.Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(Constant.BASE_URL)
                    .build()

            return retrofit.create(NotifService::class.java)
        }
    }
}