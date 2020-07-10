package com.taller2.chotuve.modelo.interactor

import android.util.Log
import com.taller2.chotuve.modelo.*
import com.taller2.chotuve.modelo.data.ResponderSolicitudDeContactoData
import com.taller2.chotuve.modelo.data.RespuestaSolicitudDeContacto
import com.taller2.chotuve.modelo.data.SolicitudDeContactoData
import com.taller2.chotuve.util.deserializarUsuario
import com.taller2.chotuve.util.deserializarUsuarioId
import retrofit2.Callback
import okhttp3.ResponseBody
import org.json.JSONArray
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

    interface CallbackResponderSolicitudesDeContacto {
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

                                ret.add(deserializarUsuarioId(objeto))
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
                                        deserializarUsuario(objeto)
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

    fun rechazarSolicitud(id: Long, callback: CallbackResponderSolicitudesDeContacto) {
        responderSolicitud(id, RespuestaSolicitudDeContacto.RECHAZAR, callback)
    }

    fun aceptarSolicitud(id: Long, callback: CallbackResponderSolicitudesDeContacto) {
        responderSolicitud(id, RespuestaSolicitudDeContacto.ACEPTAR, callback)
    }

    private fun responderSolicitud(id: Long, respuesta: RespuestaSolicitudDeContacto, callback: CallbackResponderSolicitudesDeContacto) {
        chotuveClient.responderSolicitudDeContacto(id, ResponderSolicitudDeContactoData(respuesta.value)).enqueue(
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