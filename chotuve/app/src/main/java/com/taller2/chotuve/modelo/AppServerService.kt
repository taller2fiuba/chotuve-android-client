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
import java.util.concurrent.TimeUnit

interface AppServerService {
    // factory method
    companion object {
        // private const val BASE_URL: String = "http://10.0.2.2:28080/"
        private const val BASE_URL: String = "https://chotuve-app-server.herokuapp.com/"

        fun create(userToken: String? = null): AppServerService {
            var httpClient = OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)

            if (userToken != null) {
                httpClient = httpClient.addInterceptor { chain ->
                    val newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $userToken")
                        .build()
                    chain.proceed(newRequest)
                }
            }

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(httpClient.build())
                .build()
                .create(AppServerService::class.java)
        }
    }

    @POST("/usuario")
    fun registrarUsuario(@Body infoRegistro: InfoRegistro) : Call<ResponseBody>

    @POST("/usuario/sesion")
    fun iniciarSesion(@Body infoInicioSesion: InfoInicioSesion) : Call<ResponseBody>

    @POST("/video")
    fun crearVideo(@Body video: Video) : Call<ResponseBody>

    @GET("/video")
    fun obtenerVideos(@Query("cantidad") cantidad: Int = 10,
                      @Query("offset") offset: Int = 0) : Call<ResponseBody>

    @GET("/video/{id}")
    fun obtenerVideo(@Path("id") id: String) : Call<ResponseBody>

    @GET("/usuario/{id_usuario}/perfil")
    fun obtenerPerfilUsuario(@Path("id_usuario") uid : Long) : Call<ResponseBody>

    @GET("/usuario/perfil")
    fun obtenerMiPerfil() : Call<ResponseBody>
}