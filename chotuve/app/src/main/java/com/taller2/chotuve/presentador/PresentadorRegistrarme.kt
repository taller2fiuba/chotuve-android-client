package com.taller2.chotuve.presentador

import com.taller2.chotuve.modelo.CallbackRegistro
import com.taller2.chotuve.modelo.interactor.InteractorIniciarSesion
import com.taller2.chotuve.modelo.interactor.InteractorRegistrarme
import com.taller2.chotuve.vista.autenticacion.VistaIniciarSesion
import com.taller2.chotuve.vista.autenticacion.VistaRegistrarme

class PresentadorRegistrarme(var vista: VistaRegistrarme?,
                             val interactorRegistrarme: InteractorRegistrarme
) {
    fun onCreate() {
        if (interactorRegistrarme.estaLogueado()) {
            vista?.irAPantallaPrincipal()
        }
    }

    fun onDestroy() {
        vista = null
    }


    fun registrarme(email: String, clave: String) {
        interactorRegistrarme.registrarme(email, clave, object : CallbackRegistro {
            override fun onExito() {
                vista?.irAPantallaPrincipal()
            }

            override fun onYaEstaRegistrado() {
                vista?.setUsuarioYaExiste()
            }

            override fun onErrorRed(mensaje: String?) {
                vista?.setErrorRed()
            }
        })
    }
}