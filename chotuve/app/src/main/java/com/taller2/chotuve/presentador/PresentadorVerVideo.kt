package com.taller2.chotuve.presentador

import android.os.Handler
import android.util.Log
import com.taller2.chotuve.modelo.Video
import com.taller2.chotuve.modelo.interactor.InteractorPrincipal
import com.taller2.chotuve.modelo.interactor.InteractorVerVideo
import com.taller2.chotuve.vista.principal.VistaPrincipal
import com.taller2.chotuve.vista.ver_video.VistaVerVideo

class PresentadorVerVideo (private val vista: VistaVerVideo,
                           private val interactor: InteractorVerVideo
) {
    fun onDestroy() {
    }

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
}