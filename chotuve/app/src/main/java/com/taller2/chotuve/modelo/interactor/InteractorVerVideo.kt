package com.taller2.chotuve.modelo.interactor

import android.util.Log
import com.taller2.chotuve.modelo.*
import com.taller2.chotuve.modelo.data.ComentarioData
import com.taller2.chotuve.util.deserializarUsuario
import com.taller2.chotuve.util.deserializarUsuarioId
import com.taller2.chotuve.util.obtenerFechaDeIso8601
import retrofit2.Callback
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class InteractorVerVideo {
    interface CallbackVerVideo {
        fun onVideoObtenido(video: Video)
        fun onError(mensaje: String)
    }

    interface CallbackReaccionar {
        fun onErrorRed()
    }

    interface CallbackComentar {
        fun onComentarioCreado(comentario: String, miPerfil: PerfilDeUsuario)
        fun onErrorRed()
    }

    interface CallbackVerComentarios {
        fun onExito(comentarios: List<Comentario>)
        fun onErrorRed()
    }

    private val chotuveClient = Modelo.instance.chotuveClient
    private val interactorPerfil = InteractorPerfil()

    fun obtenerVideo(id: String, callbackVerVideo: CallbackVerVideo) {
        chotuveClient.obtenerVideo(id).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                when (response.code()) {
                    200 -> {
                        val objeto = JSONObject(response.body()!!.string())

                        val autorJson = objeto.getJSONObject("autor")
                        val autor = deserializarUsuario(autorJson)

                        val miReaccion = if (objeto.getString("mi-reaccion") != "null") Reaccion.getByValue(objeto.getString("mi-reaccion")) else null
                        val reacciones = Reacciones(
                            objeto.getString("me-gustas").toLong(),
                            objeto.getString("no-me-gustas").toLong(),
                            miReaccion
                        )

                        callbackVerVideo.onVideoObtenido(Video(
                            objeto.getString("url"),
                            objeto.getString("id"),
                            objeto.getString("titulo"),
                            autor,
                            obtenerFechaDeIso8601(objeto.getString("creacion")),
                            objeto.getString("descripcion"),
                            objeto.getLong("duracion"),
                            objeto.getLong("cantidad-comentarios"),
                            reacciones
                        ))
                    }
                    400 -> callbackVerVideo.onError("Error interno")
                    404 -> callbackVerVideo.onError("El video no existe")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callbackVerVideo.onError(t.message.toString())
            }
        })
    }

    fun reaccionar(videoId: String, reaccion: Reaccion, callbackReaccionar: CallbackReaccionar) {
        chotuveClient.reaccionar(videoId, com.taller2.chotuve.modelo.data.Reaccion(reaccion.value)).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callbackReaccionar.onErrorRed()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {}
        })
    }

    fun crearComentario(videoId: String, comentario: String, callbackComentar: CallbackComentar) {
        chotuveClient.comentar(videoId, ComentarioData(comentario)).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callbackComentar.onErrorRed()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responseCode = response.code()
                if (responseCode == 201) {
                    Log.d("modelo", "Comentario creado")
                    interactorPerfil.cargarMiPerfil(object : InteractorPerfil.CallbackCargarPerfil {
                        override fun onExito(perfilDeUsuario: PerfilDeUsuario) {
                            callbackComentar.onComentarioCreado(comentario, perfilDeUsuario)
                        }

                        override fun onError() {
                            Log.d("modelo", "Error al comentar")
                        }

                        override fun onErrorRed() {
                            Log.d("modelo", "Error al comentar")
                            callbackComentar.onErrorRed()
                        }
                    })
                } else {
                    Log.d("modelo", "Error al comentar")
                    callbackComentar.onErrorRed()
                }
            }
        })
    }

    fun obtenerComentarios(videoId: String, pagina: Int, callbackVerComentarios: CallbackVerComentarios) {
        chotuveClient.obtenerComentarios(videoId, 10, pagina * 10).enqueue(
            object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.d("I/VerVideo", "Comentarios obtenidos")
                    when (response.code()) {
                        200 -> callbackVerComentarios.onExito(deserializarComentarios(response.body()!!.string()!!))
                        400, 401, 404 -> callbackVerComentarios.onErrorRed()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    callbackVerComentarios.onErrorRed()
                }
            }
        )
    }

    private fun deserializarComentarios(jsonData: String): List<Comentario> {
        val data = JSONArray(jsonData)
        val comentarios = mutableListOf<Comentario>()

        for (i in 0 until data.length()) {
            val obj = data.getJSONObject(i)
            val autor = obj.getJSONObject("autor")

            comentarios.add(
                Comentario(
                    deserializarUsuarioId(autor),
                    obtenerFechaDeIso8601(obj.getString("fecha")),
                    obj.getString("comentario")
                )
            )
        }

        return comentarios
    }
}