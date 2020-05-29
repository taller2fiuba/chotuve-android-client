package com.taller2.chotuve.presentador

import com.taller2.chotuve.modelo.Video
import com.taller2.chotuve.modelo.interactor.InteractorPrincipal
import com.taller2.chotuve.vista.principal.VistaPrincipal

class PresentadorPrincipal (private val vista: VistaPrincipal,
                            private val interactor: InteractorPrincipal
) {
    fun onDestroy() {

    }

    fun obtenerVideos(pagina: Int) {
        interactor.obtenerVideos(pagina, object : InteractorPrincipal.CallbackObtenerVideo {
            override fun onObtenerExitoso(videos: List<Video>) {
                vista.mostrarVideos(videos)
            }
            override fun onErrorRed(mensaje: String?) {
                vista.setErrorRed()
            }
        })
    }
}