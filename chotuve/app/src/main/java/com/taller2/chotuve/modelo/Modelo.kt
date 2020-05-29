package com.taller2.chotuve.modelo

import android.content.Context
import android.util.Log
import com.taller2.chotuve.Chotuve
import com.taller2.chotuve.modelo.data.InfoInicioSesion
import org.json.JSONObject;
import com.taller2.chotuve.modelo.data.InfoRegistro
import com.taller2.chotuve.modelo.data.Video
import com.taller2.chotuve.modelo.interactor.InteractorPrincipal
import okhttp3.ResponseBody
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Call

class Modelo private constructor() {
    // Singleton
    companion object {
        val instance = Modelo()
    }

    private val appContext = Chotuve.context
    private val preferences = appContext.getSharedPreferences(appContext.packageName, Context.MODE_PRIVATE)

    private var userToken: String? = preferences.getString("token", null)
        set(token) {
            with (preferences.edit()) {
                putString("token", token)
                commit()
            }
            field = token

            // Actualizar el cliente
            chotuveClient = AppServerService.create(userToken)
        }

    private var chotuveClient = AppServerService.create(userToken)

    fun estaLogueado() : Boolean = userToken != null

    fun registrarUsuario(email: String, clave: String, callbackRegistro: CallbackRegistro) {
        Log.d("modelo", "Registrando usuario $email")
        chotuveClient.registrarUsuario(
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

    fun iniciarSesion(email: String, clave: String, callbackInicioSesion: CallbackInicioSesion) {
        Log.d("modelo", "Iniciando sesión de usuario " + email)
        chotuveClient.iniciarSesion(
            InfoInicioSesion(
                email,
                clave
            )
        ).enqueue(
            object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.d("modelo", "Respuesta obtenida: " + response.code())
                    if (response.code() == 200) {
                        val json = JSONObject(response.body()!!.string())
                        if (!json.has("auth_token")) {
                            Log.d("modelo", "No hay auth_token")
                            callbackInicioSesion.onErrorRed("Error desconocido: no hay auth_token")
                        } else {
                            Log.d("modelo", "Token obtenido")
                            userToken = json.getString("auth_token")
                            callbackInicioSesion.onExito()
                        }
                    } else if (response.code() == 400) {
                        Log.d("modelo", "Usuario ya registrado")
                        callbackInicioSesion.onUsuarioOClaveIncorrecta()
                    } else {
                        Log.d("modelo", "Error desconocido " + response.code())
                        callbackInicioSesion.onErrorRed("Error desconocido: " + response.code())
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("modelo", "Error: " + t.message)
                    callbackInicioSesion.onErrorRed(t.message)
                }
            }
        )
    }

    fun subirVideo(titulo: String, url: String, callbackSubirVideo: CallbackSubirVideo) {
        Log.d("modelo", "Subiendo video $titulo con url $url")
        chotuveClient.crearVideo(Video(titulo, url))
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    val responseCode = response.code()
                    if (responseCode == 201) {
                        Log.d("modelo", "Subido al app server")
                        callbackSubirVideo.onExito(url)
                    } else {
                        Log.d("modelo", "Error: " + response.body()!!.string())
                        callbackSubirVideo.onErrorRed(response.body()!!.string())
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("modelo", "Error al contactar al app server: " + t.message)
                    callbackSubirVideo.onErrorRed(t.message)
                }
            })
    }
}