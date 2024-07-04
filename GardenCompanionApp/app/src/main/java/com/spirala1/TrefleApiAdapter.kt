package com.spirala1

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TrefleApiAdapter {
    private const val BASE_URL = "http://trefle.io/api/v1/"

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}