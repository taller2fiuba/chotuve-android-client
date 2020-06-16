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
        val handler = Handler()
        handler.postDelayed(
            Runnable {
                if (pagina == 0) {
                    val comentarios = listOf<Comentario>(
                        Comentario(Autor(1, "franco"), "16/04/2020", "hola todo bien?"),
                        Comentario(Autor(1, "lucho"), "17/04/2020", "hola todo bien2?"),
                        Comentario(Autor(1, "mati"), "18/04/2020", "hola todo bien3?"),
                        Comentario(Autor(1, "edson"), "19/04/2020", "hola todo bien4?"),
                        Comentario(Autor(1, "lucho"), "17/04/2020", "hola todo bien5?"),
                        Comentario(Autor(1, "lucho"), "17/04/2020", "hola todo bien6?"),
                        Comentario(Autor(1, "lucho"), "17/04/2020", "hola todo bien7?"),
                        Comentario(Autor(1, "lucho"), "17/04/2020", "hola todo bien8?"),
                        Comentario(Autor(1, "lucho"), "17/04/2020", "hola todo bien9?"),
                        Comentario(Autor(1, "lucho"), "17/04/2020", "hola todo bien10?")
                    )
                    vista.mostrarComentarios(comentarios)
                } else if (pagina == 1){
                    val comentarios = listOf<Comentario>(
                        Comentario(Autor(1, "franco"), "16/04/2020", "hola todo bien11?"),
                        Comentario(Autor(1, "lucho"), "17/04/2020", "hola todo bien12?")
                    )
                    vista.mostrarComentarios(comentarios)
                } else {
                    vista.mostrarComentarios(emptyList())
                }
            },
            1000
        )
    }
}