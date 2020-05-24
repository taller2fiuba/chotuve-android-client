package com.taller2.chotuve.modelo.interactor

import com.taller2.chotuve.modelo.CallbackInicioSesion
import com.taller2.chotuve.modelo.Modelo

class InteractorIniciarSesion {
    private val modelo = Modelo.instance

    fun iniciarSesion(usuario: String, clave: String, listener: CallbackInicioSesion) {
        modelo.iniciarSesion(usuario, clave, listener)
    }

    fun estaLogueado() = modelo.estaLogueado()
}