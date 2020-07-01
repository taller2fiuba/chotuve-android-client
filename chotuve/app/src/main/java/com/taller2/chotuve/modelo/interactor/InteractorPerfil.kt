package com.taller2.chotuve.modelo.interactor

import com.taller2.chotuve.modelo.Modelo
import com.taller2.chotuve.modelo.PerfilDeUsuario
import com.taller2.chotuve.modelo.Usuario
import retrofit2.Callback
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class InteractorPerfil {
    interface CallbackCargarPerfil {
        fun onExito(perfilDeUsuario: PerfilDeUsuario)
        fun onError()
        fun onErrorRed()
    }

    private val chotuveClient = Modelo.instance.chotuveClient

    fun cargarPerfil(usuarioId: Long, callbackCargarPerfil: CallbackCargarPerfil) {
        chotuveClient.obtenerPerfilUsuario(usuarioId).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                when (response.code()) {
                    200 -> callbackCargarPerfil.onExito(deserializar(response.body()!!.string()!!))
                    400, 401, 404 -> callbackCargarPerfil.onError()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callbackCargarPerfil.onErrorRed()
            }
        })
    }

    fun cargarMiPerfil(callbackCargarPerfil: CallbackCargarPerfil) {
        chotuveClient.obtenerMiPerfil().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                when (response.code()) {
                    200 -> callbackCargarPerfil.onExito(deserializar(response.body()!!.string()!!))
                    400, 401, 404 -> callbackCargarPerfil.onError()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callbackCargarPerfil.onErrorRed()
            }
        })
    }

    private fun deserializar(body: String): PerfilDeUsuario {
        val data = JSONObject(body)
        return PerfilDeUsuario(
            Usuario(data.getLong("id"), getString(data, "email")!!),
            getString(data, "nombre"),
            getString(data, "apellido"),
            getString(data, "telefono"),
            getString(data, "direccion"),
            getString(data, "foto")
        )
    }

    private fun getString(data: JSONObject, campo: String): String? {
        val valor = data.getString(campo)
        if (valor != "null" && valor != "") {
            return valor
        }
        return null
    }
}