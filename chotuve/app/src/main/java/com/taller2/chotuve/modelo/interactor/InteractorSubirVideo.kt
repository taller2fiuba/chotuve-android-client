package com.taller2.chotuve.modelo.interactor

import android.net.Uri
import com.taller2.chotuve.modelo.Modelo
import com.taller2.chotuve.util.obtenerDuracionVideo

class InteractorSubirVideo {
    interface CallbackCrearVideo {
        fun onExito()
        fun onError()
        fun onErrorRed()
    }

    private val modelo = Modelo.instance

    fun crearVideo(titulo: String, ubicacion: String, descripcion: String?, visibilidad: String, uri: Uri, urlDescargaVideo: String, callbackCrearVideo: CallbackCrearVideo) {
        modelo.crearVideo(titulo, obtenerDuracionVideo(uri) / 1000, ubicacion, descripcion ,visibilidad, urlDescargaVideo, object : com.taller2.chotuve.modelo.CallbackCrearVideo {
            override fun onExito(url: String) {
                callbackCrearVideo.onExito()
            }

            override fun onError() {
                callbackCrearVideo.onError()
            }

            override fun onErrorRed(mensaje: String?) {
                callbackCrearVideo.onErrorRed()
            }
        })
    }


}