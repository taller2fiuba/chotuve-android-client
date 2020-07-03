package com.taller2.chotuve.modelo.interactor

import android.util.Log
import com.taller2.chotuve.modelo.*
import com.taller2.chotuve.modelo.data.ComentarioData
import retrofit2.Callback
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat

class InteractorVerVideo {
    interface CallbackVerVideo {
        fun onVideoObtenido(video: Video)
        fun onError(mensaje: String)
    }

    interface CallbackReaccionar {
        fun onErrorRed()
    }

    interface CallbackComentar {
        fun onComentarioCreado(comentario: String)
        fun onErrorRed()
    }

    interface CallbackVerComentarios {
        fun onExito(comentarios: List<Comentario>)
        fun onErrorRed()
    }

    private val chotuveClient = Modelo.instance.chotuveClient

    fun obtenerVideo(id: String, callbackVerVideo: CallbackVerVideo) {
        chotuveClient.obtenerVideo(id).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                when (response.code()) {
                    200 -> {
                        val iso8601Format =
                            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                        val dmyFormat = SimpleDateFormat("dd/MM/yyyy")

                        val objeto = JSONObject(response.body()!!.string())
                        val creacion = iso8601Format.parse(objeto.getString("creacion"))

                        val autorJson = objeto.getJSONObject("autor")
                        val autor = Usuario(autorJson.getString("usuario_id").toLong(), autorJson.getString("email"))

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
                            dmyFormat.format(creacion!!),
                            objeto.getString("descripcion"),
                            objeto.getLong("duracion"),
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
                    callbackComentar.onComentarioCreado(comentario)
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
        // callbackVerComentarios.onExito(listOf(Comentario(Autor(1, "franco"), "16/04/2020", "hola todo bien?")))
    }

    private fun deserializarComentarios(jsonData: String): List<Comentario> {
        val iso8601Format =
            SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss.SSS")
        val dmyFormat = SimpleDateFormat("dd/MM/yyyy")
        val data = JSONArray(jsonData)
        val comentarios = mutableListOf<Comentario>()

        for (i in 0 until data.length()) {
            val obj = data.getJSONObject(i)
            val autor = obj.getJSONObject("autor")
            var fecha = obj.getString("fecha")
            // Cortar el +00:00 que API 23 no entiende
            fecha = fecha.substring(0, fecha.length - 6)

            comentarios.add(
                Comentario(
                    Usuario(
                        autor.getLong("id"),
                        autor.getString("email")
                    ),
                    dmyFormat.format(
                        iso8601Format.parse(obj.getString("fecha"))!!
                    ),
                    obj.getString("comentario")
                )
            )
        }

        return comentarios
    }
}