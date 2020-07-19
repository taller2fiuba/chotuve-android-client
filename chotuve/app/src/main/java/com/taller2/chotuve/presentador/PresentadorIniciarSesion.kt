package com.taller2.chotuve.presentador

import com.taller2.chotuve.modelo.CallbackInicioSesion
import com.taller2.chotuve.modelo.Modelo
import com.taller2.chotuve.modelo.interactor.InteractorIniciarSesion
import com.taller2.chotuve.vista.autenticacion.VistaIniciarSesion

class PresentadorIniciarSesion(var vistaIniciarSesion: VistaIniciarSesion?,
                               val interactorIniciarSesion: InteractorIniciarSesion
) {
    fun onCreate() {
        if (interactorIniciarSesion.estaLogueado())
            vistaIniciarSesion?.irAPantallaPrincipal()
    }

    fun iniciarSesion(email: String, clave: String) {
        interactorIniciarSesion.iniciarSesion(email, clave, object : CallbackInicioSesion {
            override fun onExito() {
                vistaIniciarSesion?.irAPantallaPrincipal()
            }

            override fun onUsuarioOClaveIncorrecta() {
                vistaIniciarSesion?.setUsuarioOClaveIncorrecta()
            }

            override fun onErrorRed(mensaje: String?) {
                vistaIniciarSesion?.setErrorRed()
            }
        })
    }
}