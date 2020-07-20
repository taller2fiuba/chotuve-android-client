package com.taller2.chotuve.modelo.interactor

import android.util.Log
import com.taller2.chotuve.modelo.Modelo
import com.taller2.chotuve.modelo.Video
import com.taller2.chotuve.modelo.interactor.desearilizador.DeserializadorUsuario
import com.taller2.chotuve.util.deserializarUsuario
import com.taller2.chotuve.util.obtenerFechaDeIso8601
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InteractorVideos(val deserializadorUsuario: DeserializadorUsuario) {
    private val CANTIDAD_VIDEOS_POR_PAGINA = 10

    interface CallbackObtenerVideo {
        fun onObtenerExitoso(videos: List<Video>)
        fun onErrorRed(mensaje: String?)
    }

    private val chotuveClient = Modelo.instance.chotuveClient

    fun obtenerVideos(pagina: Int, callback: CallbackObtenerVideo) {
        obtenerVideosDeUsuarioInterno(
            chotuveClient.obtenerVideos(
                CANTIDAD_VIDEOS_POR_PAGINA,
                pagina * CANTIDAD_VIDEOS_POR_PAGINA),
            callback)
    }

    fun obtenerVideosDeUsuario(usuarioId: Long, pagina: Int, callback: CallbackObtenerVideo) {
        obtenerVideosDeUsuarioInterno(
            chotuveClient.obtenerVideosDeUsuario(
                usuarioId,
                CANTIDAD_VIDEOS_POR_PAGINA,
                pagina * CANTIDAD_VIDEOS_POR_PAGINA),
            callback)
    }

    private fun obtenerVideosDeUsuarioInterno(call: Call<ResponseBody>, callback: InteractorVideos.CallbackObtenerVideo) {
        call.enqueue(
            object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.d("InteractorVideos", "Respuesta obtenida: " + response.code())
                    when (response.code()) {
                        200 -> {
                            val json = JSONArray(response.body()!!.string())
                            val ret = mutableListOf<Video>()

                            for (i in 0 until json.length()) {
                                val objeto = json.getJSONObject(i)

                                ret.add(Video(
                                    objeto.getString("url"),
                                    objeto.getString("id"),
                                    objeto.getString("titulo"),
                                    deserializadorUsuario.deserializar(objeto),
                                    obtenerFechaDeIso8601(objeto.getString("creacion")),
                                    objeto.getString("descripcion"),
                                    objeto.getLong("duracion"),
                                    objeto.getLong("cantidad-comentarios")
                                ))
                            }
                            callback.onObtenerExitoso(ret)
                        }
                        400 -> callback.onErrorRed("Error interno")
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("modelo", "Error: " + t.message)
                    callback.onErrorRed(t.message)
                }
            }
        )
    }
}