package com.taller2.chotuve.presentador

import android.os.Handler
import android.util.Log
import com.taller2.chotuve.modelo.*
import com.taller2.chotuve.modelo.interactor.InteractorVerVideo
import com.taller2.chotuve.vista.ver_video.VistaVerVideo

class PresentadorVerVideo (private val vista: VistaVerVideo,
                           private val interactor: InteractorVerVideo
) {
    fun obtenerVideo(id: String) {
        interactor.obtenerVideo(id, object : InteractorVerVideo.CallbackVerVideo {
            override fun onVideoObtenido(video: Video) {
                vista.mostrarVideo(video)
            }

            override fun onError(mensaje: String) {
                Log.d("P/VerVideo", "Error: $mensaje")
            }
        })
    }

    fun reaccionar(videoId: String, reaccion: Reaccion) {
        interactor.reaccionar(videoId, reaccion, object : InteractorVerVideo.CallbackReaccionar {
            override fun onErrorRed() {
                vista.setErrorRed()
            }
        })
    }

    fun obtenerComentarios(videoId: String, pagina: Int) {
        interactor.obtenerComentarios(videoId, pagina, object : InteractorVerVideo.CallbackVerComentarios {
            override fun onExito(comentarios: List<Comentario>) {
                vista.mostrarComentarios(comentarios)
            }

            override fun onErrorRed() {
                vista.setErrorRed()
            }
        })
    }

    fun crearComentario(videoId: String, comentario: String) {
        interactor.crearComentario(videoId, comentario, object : InteractorVerVideo.CallbackComentar {
            override fun onErrorRed() {
                vista.setErrorRed()
            }

            override fun onComentarioCreado(comentario: String) {
                vista.agregarNuevoComentario(comentario)
            }
        })
    }
}