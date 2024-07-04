package com.spirala1

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TrefleApi {
    @GET("plants/search")
    suspend fun searchPlants(
        @Query("q") query: String,
        @Query("token") token: String
    ): Response<TrefleImageResponse>

    @GET("plants/{id}")
    suspend fun getPlantDetails(
        @Path("id") id: Int,
        @Query("token") token: String
    ): Response<TrefleFixDataResponse>

    @GET("species/search")
    suspend fun getPlantsWithColor(
        @Query("q") substring: String,
        @Query("filter[flower_color]") color: String,
        @Query("token") token: String
    ): Response<TrefleColorResponse>





}
