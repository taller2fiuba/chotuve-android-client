package com.taller2.chotuve.modelo.interactor

import com.taller2.chotuve.modelo.Modelo
import com.taller2.chotuve.modelo.data.CambioClaveData
import retrofit2.Callback
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class InteractorCambiarClave {
    interface CallbackCambiarClave {
        fun onExito()
        fun onError()
        fun onErrorRed()
    }

    private val chotuveClient = Modelo.instance.chotuveClient

    fun cambiarClave(claveActual: String, nuevaClave: String, callback: CallbackCambiarClave) {
        chotuveClient.cambiarClave(CambioClaveData(claveActual, nuevaClave)).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                when (response.code()) {
                    200 -> callback.onExito()
                    400 -> callback.onError()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callback.onErrorRed()
            }
        })
    }
}