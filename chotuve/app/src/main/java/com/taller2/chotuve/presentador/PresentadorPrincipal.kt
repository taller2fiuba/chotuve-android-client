package com.taller2.chotuve.presentador

import android.net.Uri
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
            override fun onObtenerExitoso(uri: Uri, duracion: Long, titulo: String, autor: String, creacion: String) {
                vista.mostrarVideo(uri, duracion, titulo, autor, creacion)
            }
            override fun onErrorRed(mensaje: String?) {
                vista.setErrorRed()
            }
        })
    }
}