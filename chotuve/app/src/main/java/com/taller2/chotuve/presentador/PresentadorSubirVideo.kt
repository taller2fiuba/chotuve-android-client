package com.taller2.chotuve.presentador

import android.net.Uri
import com.taller2.chotuve.modelo.interactor.InteractorFirebase
import com.taller2.chotuve.modelo.interactor.InteractorSubirVideo
import com.taller2.chotuve.vista.perfil.VistaSubirVideo

class PresentadorSubirVideo (private var vista: VistaSubirVideo?,
                             private val interactorSubirVideo: InteractorSubirVideo
) {
    private val interactorFirebase: InteractorFirebase = InteractorFirebase("videos")
    private lateinit var uri: Uri

    fun onDestroy() {
        vista = null
    }

    fun elegirVideo(uri: Uri) {
        vista?.deshabilitarBotonSubidaAppServer()
        this.uri = uri
        interactorFirebase.subir(uri, object : InteractorFirebase.CallbackSubir {
            override fun onSubidaExitosa() {
                vista?.setProgresoSubidaFirebase(100)
                vista?.habilitarBotonSubidaAppServer()
            }

            override fun onActualizarProgreso(progreso: Int) {
                vista?.setProgresoSubidaFirebase(progreso)
            }

            override fun onErrorSubida() {
                vista?.setErrorRed()
            }
        })
    }

    fun crearVideo(titulo: String, ubicacion: String, descripcion: String?, visibilidad: String) {
        vista?.deshabilitarBotonSubidaAppServer()
        vista?.mostrarProgresoSubidaAppServer()
        interactorSubirVideo.crearVideo(titulo, ubicacion, descripcion, visibilidad, uri, interactorFirebase.urlDescarga!!, object : InteractorSubirVideo.CallbackCrearVideo {
            override fun onExito() {
                vista?.onSubidaAppServerExitosa()
            }

            override fun onError() {
                vista?.ocultarProgresoSubidaAppServer()
                vista?.habilitarBotonSubidaAppServer()
                vista?.setError()
            }

            override fun onErrorRed() {
                vista?.ocultarProgresoSubidaAppServer()
                vista?.habilitarBotonSubidaAppServer()
                vista?.setErrorRed()
            }
        })
    }
}