package com.taller2.chotuve.presentador

import com.taller2.chotuve.modelo.CallbackRegistro
import com.taller2.chotuve.modelo.interactor.InteractorIniciarSesion
import com.taller2.chotuve.modelo.interactor.InteractorRegistrarme
import com.taller2.chotuve.vista.autenticacion.VistaIniciarSesion
import com.taller2.chotuve.vista.autenticacion.VistaRegistrarme

class PresentadorRegistrarme(var vistaRegistrarme: VistaRegistrarme?,
                             val interactorRegistrarme: InteractorRegistrarme
) {
    fun onCreate() {
        if (interactorRegistrarme.estaLogueado()) {
            vistaRegistrarme?.irAPantallaPrincipal()
        }
    }

    fun registrarme(usuario: String, clave: String) {
        interactorRegistrarme.registrarme(usuario, clave, object : CallbackRegistro {
            override fun onExito() {
                vistaRegistrarme?.irAPantallaPrincipal()
            }

            override fun onYaEstaRegistrado() {
                vistaRegistrarme?.setUsuarioYaExiste()
            }

            override fun onErrorRed(mensaje: String?) {
                vistaRegistrarme?.setErrorRed()
            }
        })
    }
}