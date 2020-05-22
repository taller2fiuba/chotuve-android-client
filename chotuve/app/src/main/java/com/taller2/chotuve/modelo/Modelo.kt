package com.taller2.chotuve.modelo

import android.util.Log
import org.json.JSONObject;
import com.taller2.chotuve.modelo.data.InfoRegistro
import okhttp3.ResponseBody
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Call

class Modelo private constructor () {
    // Singleton
    companion object {
        val instance = Modelo()
    }

    private val appServerService = AppServerService.create()
    private var userToken: String? = null;

    fun estaLogueado() : Boolean = userToken != null

    fun registrarUsuario(email: String, clave: String, callbackRegistro: CallbackRegistro) {
        Log.d("modelo", "Registrando usuario " + email)
        appServerService.registrarUsuario(
            InfoRegistro(
                email,
                clave
            )
        ).enqueue(
            object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.d("modelo", "Respuesta obtenida: " + response.code())
                    if (response.code() == 201) {
                        val json = JSONObject(response.body()!!.string())
                        if (!json.has("auth_token")) {
                            Log.d("modelo", "No hay auth_token")
                            callbackRegistro.onErrorRed("Error desconocido: no hay auth_token")
                        } else {
                            Log.d("modelo", "Token obtenido")
                            userToken = json.getString("auth_token")
                            callbackRegistro.onExito()
                        }
                    } else if (response.code() == 400) {
                        Log.d("modelo", "Usuario ya registrado")
                        callbackRegistro.onYaEstaRegistrado()
                    } else {
                        Log.d("modelo", "Error desconocido " + response.code())
                        callbackRegistro.onErrorRed("Error desconocido: " + response.code())
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("modelo", "Error: " + t.message)
                    callbackRegistro.onErrorRed(t.message)
                }
            }
        )
    }
}