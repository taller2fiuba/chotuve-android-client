package com.taller2.chotuve.presentador

import android.util.Log
import com.taller2.chotuve.modelo.PerfilDeUsuario
import com.taller2.chotuve.modelo.interactor.InteractorContactos
import com.taller2.chotuve.modelo.interactor.InteractorPerfil
import com.taller2.chotuve.vista.perfil.VistaInformacion


class PresentadorPerfil (private var vista: VistaInformacion) {
    private val interactor = InteractorPerfil()
    private val interactorContactos = InteractorContactos()

    fun obtenerInformacion(usuarioId: Long?) {
        val callback = object : InteractorPerfil.CallbackCargarPerfil {
            override fun onExito(perfilDeUsuario: PerfilDeUsuario) {
                vista.mostrarPerfil(perfilDeUsuario)
            }

            override fun onError() {
                // ?
                Log.d("P/Perfil", "Error genérico")
            }

            override fun onErrorRed() {
                // ?
                Log.d("P/Perfil", "Error de red")
            }
        }

        if (usuarioId == null)
            interactor.cargarMiPerfil(callback)
        else
            interactor.cargarPerfil(usuarioId, callback)
    }

    fun enviarSolicitudDeContacto(usuarioId: Long) {
        interactorContactos.enviarSolicitudDeContacto(usuarioId, object : InteractorContactos.CallbackEnviarSolicitudDeContacto {
            override fun onErrorRed(mensaje: String?) {
                vista.setErrorRed()
            }
        })
    }
}