package com.taller2.chotuve.presentador

import android.net.Uri
import com.taller2.chotuve.modelo.interactor.InteractorSubirVideo
import com.taller2.chotuve.vista.perfil.VistaSubirVideo

class PresentadorSubirVideo (private val vistaSubirVideo: VistaSubirVideo,
                             private val interactorSubirVideo: InteractorSubirVideo
) {
    fun onDestroy() {

    }

    fun elegirVideo(uri: Uri) {
        vistaSubirVideo.deshabilitarBotonSubidaAppServer()
        interactorSubirVideo.subirVideoAFirebase(uri, object : InteractorSubirVideo.CallbackSubirVideo {
            override fun onSubidaExitosa() {
                vistaSubirVideo.setProgresoSubidaFirebase(100)
                vistaSubirVideo.habilitarBotonSubidaAppServer()
            }

            override fun onActualizarProgreso(progreso: Int) {
                vistaSubirVideo.setProgresoSubidaFirebase(progreso)
            }

            override fun onErrorSubida() {
                vistaSubirVideo.setErrorRed()
            }
        })
    }

    fun crearVideo(titulo: String) {
        vistaSubirVideo.deshabilitarBotonSubidaAppServer()
        vistaSubirVideo.mostrarProgresoSubidaAppServer()
        interactorSubirVideo.crearVideo(titulo, object : InteractorSubirVideo.CallbackSubirVideo {
            override fun onSubidaExitosa() {
                vistaSubirVideo.onSubidaAppServerExitosa()
            }

            override fun onErrorSubida() {
                vistaSubirVideo.ocultarProgresoSubidaAppServer()
                vistaSubirVideo.habilitarBotonSubidaAppServer()
                vistaSubirVideo.setErrorRed()
            }

            override fun onActualizarProgreso(progreso: Int) {
                // No hay actualización de progreso
            }
        })
    }
}