package com.taller2.chotuve.presentador

import com.taller2.chotuve.modelo.CallbackInicioSesion
import com.taller2.chotuve.modelo.Modelo
import com.taller2.chotuve.modelo.interactor.InteractorIniciarSesion
import com.taller2.chotuve.vista.autenticacion.VistaIniciarSesion

class PresentadorIniciarSesion(var vista: VistaIniciarSesion?,
                               val interactorIniciarSesion: InteractorIniciarSesion
) {
    fun onCreate() {
        if (interactorIniciarSesion.estaLogueado())
            vista?.irAPantallaPrincipal()
    }

    fun onDestroy() {
        vista = null
    }

    fun iniciarSesion(email: String, clave: String) {
        interactorIniciarSesion.iniciarSesion(email, clave, object : CallbackInicioSesion {
            override fun onExito() {
                vista?.irAPantallaPrincipal()
            }

            override fun onUsuarioOClaveIncorrecta() {
                vista?.setUsuarioOClaveIncorrecta()
            }

            override fun onErrorRed(mensaje: String?) {
                vista?.setErrorRed()
            }
        })
    }
}