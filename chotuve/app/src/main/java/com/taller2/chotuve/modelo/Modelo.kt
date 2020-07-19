package com.taller2.chotuve.modelo

import android.content.Context
import android.util.Log
import com.taller2.chotuve.Chotuve
import com.taller2.chotuve.modelo.data.InfoInicioSesion
import org.json.JSONObject;
import com.taller2.chotuve.modelo.data.InfoRegistro
import com.taller2.chotuve.modelo.data.Video
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
    private val firebaseMessagingService: ChotuveFirebaseMessagingService = ChotuveFirebaseMessagingService.instance

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

    var id: Long? = preferences.getString("id", null)?.toLong()
        set(id) {
            with (preferences.edit()) {
                putString("id", id.toString())
                commit()
            }
            field = id
        }

    var chotuveClient = AppServerService.create(userToken)
        private set(valor) { field = valor }
        get() { return field }

    fun estaLogueado() : Boolean = userToken != null && id != null

    fun cerrarSesion() {
        firebaseMessagingService.desasociarUsuarioAFMToken(id!!)
        // TODO deberia avisar al server del cierre de sesion para invalidar el token
        userToken = null
        id = null
    }

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
                            id = json.getLong("id")
                            firebaseMessagingService.asociarUsuarioAFMToken(id!!)
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
                            id = json.getLong("id")
                            firebaseMessagingService.asociarUsuarioAFMToken(id!!)
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

    fun crearVideo(titulo: String, duracion: Long, ubicacion: String, descripcion: String?, visibilidad: String, url: String, callbackCrearVideo: CallbackCrearVideo) {
        Log.d("modelo", "Subiendo video $titulo con url $url")
        chotuveClient.crearVideo(Video(titulo, duracion, ubicacion, descripcion, visibilidad, url))
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    val responseCode = response.code()
                    if (responseCode == 201) {
                        Log.d("modelo", "Subido al app server")
                        callbackCrearVideo.onExito(url)
                    } else {
                        Log.d("modelo", "Error al subir video")
                        callbackCrearVideo.onError()
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("modelo", "Error al contactar al app server: " + t.message)
                    callbackCrearVideo.onErrorRed(t.message)
                }
            })
    }
}