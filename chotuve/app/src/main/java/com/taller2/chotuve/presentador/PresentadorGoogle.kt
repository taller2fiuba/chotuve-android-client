package com.taller2.chotuve.presentador

import com.taller2.chotuve.modelo.CallbackInicioSesion
import com.taller2.chotuve.modelo.CallbackRegistro
import com.taller2.chotuve.modelo.interactor.InteractorIniciarSesion
import com.taller2.chotuve.modelo.interactor.InteractorRegistrarme
import com.taller2.chotuve.vista.autenticacion.VistaGoogle

class PresentadorGoogle(private var vista: VistaGoogle?) {
    val interactorRegistrarme = InteractorRegistrarme()
    val interactorIniciarSesion = InteractorIniciarSesion()

    fun onCreate() {
        if (interactorRegistrarme.estaLogueado()) {
            vista?.irAPantallaPrincipal()
        }
    }

    fun onDestroy() {
        vista = null
    }


    fun registrarme(email: String, id: String) {
        interactorRegistrarme.registrarme(email, id, object : CallbackRegistro {
            override fun onExito() {
                vista?.irAPantallaPrincipal()
            }

            override fun onYaEstaRegistrado() {
                iniciarSesion(email, id)
            }

            override fun onErrorRed(mensaje: String?) {
                vista?.setErrorRed()
            }
        })
    }

    fun iniciarSesion(email: String, id: String) {
        interactorIniciarSesion.iniciarSesion(email, id, object : CallbackInicioSesion {
            override fun onExito() {
                vista?.irAPantallaPrincipal()
            }

            override fun onUsuarioOClaveIncorrecta() {
                registrarme(email, id)
            }

            override fun onErrorRed(mensaje: String?) {
                vista?.setErrorRed()
            }
        })
    }
}