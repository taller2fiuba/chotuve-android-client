package com.taller2.chotuve.presentador

import com.taller2.chotuve.modelo.Video
import com.taller2.chotuve.modelo.interactor.InteractorPrincipal
import com.taller2.chotuve.vista.principal.VistaPrincipal

class PresentadorPrincipal (private val vista: VistaPrincipal,
                            private val interactor: InteractorPrincipal
) {
    fun onDestroy() {

    }

    fun obtenerVideo() {
        vista.mostrarCargandoVideo()
        interactor.obtenerVideo(object : InteractorPrincipal.CallbackObtenerVideo {
            override fun onObtenerExitoso(video: Video) {
                vista.mostrarVideo(video)
            }
            override fun onErrorRed(mensaje: String?) {
                vista.setErrorRed()
            }
        })
    }
}