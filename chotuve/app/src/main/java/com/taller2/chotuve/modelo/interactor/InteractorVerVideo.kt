package com.taller2.chotuve.modelo.interactor

import com.taller2.chotuve.modelo.Autor
import com.taller2.chotuve.modelo.Modelo
import com.taller2.chotuve.modelo.Video
import retrofit2.Callback
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat

class InteractorVerVideo {
    interface CallbackVerVideo {
        fun onVideoObtenido(video: Video)
        fun onError(mensaje: String)
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
                        val autor = Autor(autorJson.getString("usuario_id").toLong(), autorJson.getString("email"))

                        callbackVerVideo.onVideoObtenido(Video(
                            objeto.getString("url"),
                            objeto.getString("id"),
                            objeto.getString("titulo"),
                            autor,
                            dmyFormat.format(creacion!!),
                            objeto.getString("descripcion"),
                            objeto.getLong("duracion")
                        ))
                    }
                    400 -> callbackVerVideo.onError("Error interno")
                    404 -> callbackVerVideo.onError("El video no existe")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callbackVerVideo.onError(t?.message.toString())
            }
        })
    }
}