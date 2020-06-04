package com.taller2.chotuve.modelo.interactor

import android.text.format.DateFormat
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.taller2.chotuve.modelo.Modelo
import com.taller2.chotuve.modelo.Video
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDateTime

class InteractorPrincipal {
    interface CallbackObtenerVideo {
        fun onObtenerExitoso(videos: List<Video>)
        fun onErrorRed(mensaje: String?)
    }

    private val chotuveClient = Modelo.instance.chotuveClient
    private val firebaseStorage = FirebaseStorage.getInstance()

    fun obtenerVideos(pagina: Int, callback: CallbackObtenerVideo) {
        chotuveClient.obtenerVideos(10, pagina * 10).enqueue(
            object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.d("InteractorPrincipal", "Respuesta obtenida: " + response.code())
                    when (response.code()) {
                        200 -> {
                            val json = JSONArray(response.body()!!.string())
                            val ret = mutableListOf<Video>()
                            val iso8601Format =
                                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                            val dmyFormat = SimpleDateFormat("dd/MM/yyyy")
                            for (i in 0 until json.length()) {
                                val objeto = json.getJSONObject(i)
                                val creacion = iso8601Format.parse(objeto.getString("creacion"))

                                ret.add(Video(
                                    objeto.getString("url"),
                                    objeto.getString("id"),
                                    objeto.getString("titulo"),
                                    objeto.getJSONObject("autor")
                                        .getString("email"),
                                    dmyFormat.format(creacion!!),
                                    objeto.getLong("duracion")
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
            })
    }
}