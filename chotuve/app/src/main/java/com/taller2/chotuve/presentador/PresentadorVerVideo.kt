package com.taller2.chotuve.presentador

import android.util.Log
import com.taller2.chotuve.modelo.Reaccion
import com.taller2.chotuve.modelo.Video
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
}