package com.taller2.chotuve.presentador

import android.util.Log
import com.taller2.chotuve.modelo.Modelo
import com.taller2.chotuve.modelo.PerfilDeUsuario
import com.taller2.chotuve.modelo.interactor.InteractorContactos
import com.taller2.chotuve.modelo.interactor.InteractorPerfil
import com.taller2.chotuve.vista.perfil.VistaInformacion


class PresentadorPerfil (private var vista: VistaInformacion?) {
    private val interactor = InteractorPerfil()
    private val interactorContactos = InteractorContactos()

    fun onDestroy() {
        vista = null
    }

    fun obtenerInformacion(usuarioId: Long) {
        interactor.cargarPerfil(usuarioId, object : InteractorPerfil.CallbackCargarPerfil {
            override fun onExito(perfilDeUsuario: PerfilDeUsuario) {
                vista?.mostrarPerfil(perfilDeUsuario)
            }

            override fun onError() {
                Log.d("P/Perfil", "Error obteniendo perfil")
                vista?.setErrorRed()
            }

            override fun onErrorRed() {
                Log.d("P/Perfil", "Error de red")
                vista?.setErrorRed()
            }
        })
    }

    fun enviarSolicitudDeContacto(usuarioId: Long) {
        interactorContactos.enviarSolicitudDeContacto(usuarioId, object : InteractorContactos.CallbackEnviarSolicitudDeContacto {
            override fun onErrorRed(mensaje: String?) {
                vista?.setErrorRed()
            }
        })
    }

    fun rechazarSolicitudDeContacto(solicitudId: Long) {
        interactorContactos.rechazarSolicitud(solicitudId, object : InteractorContactos.CallbackResponderSolicitudesDeContacto {
            override fun onErrorRed(mensaje: String?) {
                vista?.setErrorRed()
            }
        })

    }

    fun aceptarSolicitudDeContacto(solicitudId: Long) {
        interactorContactos.aceptarSolicitud(solicitudId, object : InteractorContactos.CallbackResponderSolicitudesDeContacto {
            override fun onErrorRed(mensaje: String?) {
                vista?.setErrorRed()
            }
        })
    }

    fun cerrarSesion() {
        interactor.cerrarSesion()
    }
}