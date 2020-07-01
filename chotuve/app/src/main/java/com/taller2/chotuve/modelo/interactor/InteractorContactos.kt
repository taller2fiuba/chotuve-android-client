package com.taller2.chotuve.modelo.interactor

import android.util.Log
import com.taller2.chotuve.modelo.*
import com.taller2.chotuve.modelo.data.SolicitudDeContactoData
import retrofit2.Callback
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class InteractorContactos {
    interface CallbackObtenerContactos {
        fun onObtenerExitoso(contactos: List<Usuario>)
        fun onErrorRed(mensaje: String?)
    }

    interface CallbackObtenerSolicitudesDeContacto {
        fun onObtenerExitoso(solicitudes: List<SolicitudDeContacto>)
        fun onErrorRed(mensaje: String?)
    }

    interface CallbackEnviarSolicitudDeContacto {
        fun onErrorRed(mensaje: String?)
    }

    private val chotuveClient = Modelo.instance.chotuveClient

    fun obtenerContactos(callback: CallbackObtenerContactos) {
        chotuveClient.obtenerContactos().enqueue(
            object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.d("InteractorContactos", "Respuesta obtenida: " + response.code())
                    when (response.code()) {
                        200 -> {
                            val json = JSONArray(response.body()!!.string())
                            val ret = mutableListOf<Usuario>()
                            for (i in 0 until json.length()) {
                                val objeto = json.getJSONObject(i)

                                ret.add(Usuario(objeto.getString("id").toLong(), objeto.getString("email")))
                            }
                            callback.onObtenerExitoso(ret)
                        }
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("InteractorContactos", "Error: " + t.message)
                    callback.onErrorRed(t.message)
                }
            })
    }

    fun obtenerSolicitudesDeContacto(callback: CallbackObtenerSolicitudesDeContacto) {
        chotuveClient.obtenerSolicitudesDeContacto().enqueue(
            object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.d("InteractorContactos", "Respuesta obtenida: " + response.code())
                    when (response.code()) {
                        200 -> {
                            val json = JSONArray(response.body()!!.string())
                            val ret = mutableListOf<SolicitudDeContacto>()
                            for (i in 0 until json.length()) {
                                val objeto = json.getJSONObject(i)

                                ret.add(
                                    SolicitudDeContacto(
                                        objeto.getString("id").toLong(),
                                        Usuario(
                                            objeto.getString("usuario_id").toLong(),
                                            objeto.getString("email")
                                        )
                                    )
                                )
                            }
                            callback.onObtenerExitoso(ret)
                        }
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("InteractorContactos", "Error: " + t.message)
                    callback.onErrorRed(t.message)
                }
            }
        )
    }

    fun enviarSolicitudDeContacto(usuarioId: Long, callback: CallbackEnviarSolicitudDeContacto) {
        chotuveClient.enviarSolicitudDeContacto(SolicitudDeContactoData(usuarioId)).enqueue(
            object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.d("InteractorContactos", "Respuesta obtenida: " + response.code())
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("InteractorContactos", "Error: " + t.message)
                    callback.onErrorRed(t.message)
                }
            }
        )
    }
}