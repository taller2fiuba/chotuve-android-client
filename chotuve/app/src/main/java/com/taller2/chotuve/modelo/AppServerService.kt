package com.taller2.chotuve.modelo

import com.taller2.chotuve.modelo.data.InfoInicioSesion
import com.taller2.chotuve.modelo.data.InfoRegistro
import com.taller2.chotuve.modelo.data.Video
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface AppServerService {
    // factory method
    companion object {
        // private const val BASE_URL: String = "http://10.0.2.2:28080/"
        private const val BASE_URL: String = "https://chotuve-app-server.herokuapp.com/"

        fun create(userToken: String? = null): AppServerService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())

            if (userToken != null) {
                retrofit.client(
                    OkHttpClient.Builder().addInterceptor { chain ->
                    val newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer $userToken")
                            .build()
                        chain.proceed(newRequest)
                    }.build()
                )
            }

            return retrofit.build().create(AppServerService::class.java)
        }
    }

    @POST("/usuario")
    fun registrarUsuario(@Body infoRegistro: InfoRegistro) : Call<ResponseBody>

    @POST("/usuario/sesion")
    fun iniciarSesion(@Body infoInicioSesion: InfoInicioSesion) : Call<ResponseBody>

    @POST("/video")
    fun crearVideo(@Body video: Video) : Call<ResponseBody>

    @GET("/usuario/{id_usuario}")
    fun obtenerUsuario(@Path("id_usuario") uid : Int) : Call<ResponseBody>
}