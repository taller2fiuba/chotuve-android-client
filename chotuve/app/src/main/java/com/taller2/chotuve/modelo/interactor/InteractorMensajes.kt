package com.taller2.chotuve.modelo.interactor

import android.util.Log
import com.taller2.chotuve.modelo.*
import com.taller2.chotuve.modelo.data.NuevoMensajeData
import com.taller2.chotuve.modelo.data.ResponderSolicitudDeContactoData
import com.taller2.chotuve.modelo.data.RespuestaSolicitudDeContacto
import com.taller2.chotuve.modelo.data.SolicitudDeContactoData
import retrofit2.Callback
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class InteractorMensajes {
    interface CallbackEnviarMensaje {
        fun onObtenerExitoso()
        fun onErrorRed(mensaje: String?)
    }

    private val chotuveClient = Modelo.instance.chotuveClient

    fun enviarMensaje(destinarioId: Long, mensaje: String, callback: CallbackEnviarMensaje) {
        chotuveClient.enviarMensaje(destinarioId, NuevoMensajeData(mensaje)).enqueue(
            object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.d("InteractorMensajes", "Respuesta obtenida: " + response.code())
                    when (response.code()) {
                        201 -> callback.onObtenerExitoso()
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("InteractorMensajes", "Error: " + t.message)
                    callback.onErrorRed(t.message)
                }
            })
    }
}