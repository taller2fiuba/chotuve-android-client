package com.taller2.chotuve.presentador

import android.net.Uri
import android.util.Log
import com.taller2.chotuve.modelo.data.Perfil
import com.taller2.chotuve.modelo.interactor.InteractorEditarPerfil
import com.taller2.chotuve.modelo.interactor.InteractorFirebase
import com.taller2.chotuve.vista.perfil.VistaEditarInformacion


class PresentadorEditarPerfil(private val vista: VistaEditarInformacion) {
    private val interactorFirebase = InteractorFirebase("imagenes")
    private val interactorEditarPerfil = InteractorEditarPerfil()

    fun editarPerfil(nombre: String, apellido: String, telefono: String?, direccion: String?) {
        // puede ser null, en ese caso no mandar nada, no se esta modificando
        val fotoPerfil = interactorFirebase.urlDescarga
        Log.d("P/EditarPerfil", "Editando perfil")
        interactorEditarPerfil.editarPerfil(
            Perfil(
                nombre,
                apellido,
                telefono,
                direccion,
                fotoPerfil
            ),
            object : InteractorEditarPerfil.CallbackEditarPerfil {
                override fun onExito() {
                    Log.d("P/EditarPerfil", "Edicion exitosa")
                    vista.onEdicionExitosa()
                }

                override fun onErrorRed() {
                    vista.setErrorRed()
                }

                override fun onError() {
                    vista.setError()
                }
            }
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