package com.taller2.chotuve.modelo.interactor

import android.util.Log
import com.taller2.chotuve.modelo.Modelo
import com.taller2.chotuve.modelo.Usuario
import com.taller2.chotuve.modelo.data.Perfil
import retrofit2.Callback
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class InteractorEditarPerfil {
    interface CallbackEditarPerfil {
        fun onExito()
        fun onError()
        fun onErrorRed()
    }

    private val chotuveClient = Modelo.instance.chotuveClient

    fun editarPerfil(perfil: Perfil, callback: CallbackEditarPerfil) {
        chotuveClient.editarPerfil(perfil).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                when (response.code()) {
                    200 -> callback.onExito()
                    400, 401 -> callback.onError()
                    500 -> {
                        Log.d("I/EditarPerfil", "Error 500: " + response.errorBody()?.string())
                        callback.onError()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callback.onErrorRed()
            }
        })
    }
}