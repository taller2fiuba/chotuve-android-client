package com.taller2.chotuve.presentador

import android.net.Uri
import android.os.Handler
import com.taller2.chotuve.modelo.interactor.InteractorFirebase
import com.taller2.chotuve.vista.perfil.VistaEditarInformacion


class PresentadorEditarPerfil(private val vista: VistaEditarInformacion) {
    private val interactorFirebase: InteractorFirebase = InteractorFirebase("imagenes")

    fun editarPerfil(nombre: String, apellido: String, telefono: String?, direccion: String?) {
        // puede ser null, en ese caso no mandar nada, no se esta modificando
        val fotoPerfil = interactorFirebase.urlDescarga
        val handler = Handler()
        handler.postDelayed(
            Runnable {  vista.onEdicionExitosa() },
            1000
        )
    }

    fun subirFotoPerfil(uri: Uri) {
        vista.deshabilitarBotonEdicion()
        vista.mostrarProgresoSubidaFirebase()
        interactorFirebase.subir(uri, object : InteractorFirebase.CallbackSubir {
            override fun onSubidaExitosa() {
                vista.setProgresoSubidaFirebase(100)
                vista.habilitarBotonEdicion()
            }

            override fun onActualizarProgreso(progreso: Int) {
                vista.setProgresoSubidaFirebase(progreso)
            }

            override fun onErrorSubida() {
                vista.setErrorRed()
            }
        })
    }
}