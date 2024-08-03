package com.hninhnin.wai.services

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
    private const val BASE_URL = "http://192.168.1.191:8000/api/" // for android emulator => 10.0.2.2:8000/ or // real device => http://192.168.1.191:8000/api
    private  val Okhttp : OkHttpClient.Builder = OkHttpClient.Builder()

    private val builder: Retrofit.Builder = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(
        Okhttp.build())

    private val retrofit: Retrofit = builder.build()

    fun<T> buildService(serviceType: Class<T>) : T {
        return retrofit.create(serviceType)
    }
}