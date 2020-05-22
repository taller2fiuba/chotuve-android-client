package com.taller2.chotuve.modelo

import com.taller2.chotuve.modelo.data.InfoInicioSesion
import com.taller2.chotuve.modelo.data.InfoRegistro
import com.taller2.chotuve.modelo.data.Video
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AppServerService {

    // factory method
    companion object {
        private const val BASE_URL: String = "http://10.0.2.2:28080/" /*"https://chotuve-app-server.herokuapp.com/"*/

        fun create(): AppServerService {
            // Usé retrofit como librería para request http
            val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
            return retrofit.create(AppServerService::class.java)
        }
    }

    @GET("/ping")
    fun ping(): Call<ResponseBody>

    @POST("/video")
    fun crearVideo(@Body video: Video) : Call<ResponseBody>

    @GET("/usuario/{id_usuario}")
    fun obtenerPerfilUsuario(@Path("id_usuario") uid : Int)

    @POST("/usuario")
    fun registrarUsuario(@Body infoRegistro: InfoRegistro) : Call<ResponseBody>

    @POST("/usuario/sesion")
    fun iniciarSesion(@Body infoInicioSesion: InfoInicioSesion) : Call<ResponseBody>
}