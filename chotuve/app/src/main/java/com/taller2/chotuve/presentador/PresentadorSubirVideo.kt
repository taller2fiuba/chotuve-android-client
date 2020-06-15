package com.taller2.chotuve.presentador

import android.net.Uri
import com.taller2.chotuve.modelo.interactor.InteractorFirebase
import com.taller2.chotuve.modelo.interactor.InteractorSubirVideo
import com.taller2.chotuve.vista.perfil.VistaSubirVideo

class PresentadorSubirVideo (private val vistaSubirVideo: VistaSubirVideo,
                             private val interactorSubirVideo: InteractorSubirVideo
) {
    private val interactorFirebase: InteractorFirebase = InteractorFirebase("videos")
    private lateinit var uri: Uri

    fun elegirVideo(uri: Uri) {
        vistaSubirVideo.deshabilitarBotonSubidaAppServer()
        this.uri = uri
        interactorFirebase.subir(uri, object : InteractorFirebase.CallbackSubir {
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

    fun crearVideo(titulo: String, ubicacion: String, descripcion: String?, visibilidad: String) {
        vistaSubirVideo.deshabilitarBotonSubidaAppServer()
        vistaSubirVideo.mostrarProgresoSubidaAppServer()
        interactorSubirVideo.crearVideo(titulo, ubicacion, descripcion, visibilidad, uri, interactorFirebase.urlDescarga, object : InteractorSubirVideo.CallbackCrearVideo {
            override fun onExito() {
                vistaSubirVideo.onSubidaAppServerExitosa()
            }

            override fun onError() {
                vistaSubirVideo.ocultarProgresoSubidaAppServer()
                vistaSubirVideo.habilitarBotonSubidaAppServer()
                vistaSubirVideo.setError()
            }

            override fun onErrorRed() {
                vistaSubirVideo.ocultarProgresoSubidaAppServer()
                vistaSubirVideo.habilitarBotonSubidaAppServer()
                vistaSubirVideo.setErrorRed()
            }
        })
    }
}