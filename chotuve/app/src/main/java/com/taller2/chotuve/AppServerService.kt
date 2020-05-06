package com.taller2.chotuve

import com.google.android.gms.common.api.Api
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface AppServerService {

    // factory method
    companion object {
        private const val BASE_URL: String = "https://chotuve-app-server.herokuapp.com/"

        fun create(): AppServerService {
            val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
            return retrofit.create(AppServerService::class.java)
        }
    }

    @GET("/ping")
    fun ping(): Call<ResponseBody>
}