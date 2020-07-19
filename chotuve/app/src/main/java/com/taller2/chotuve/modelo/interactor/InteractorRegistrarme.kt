package com.taller2.chotuve.modelo.interactor

import com.taller2.chotuve.modelo.CallbackRegistro
import com.taller2.chotuve.modelo.Modelo

class InteractorRegistrarme {
    private val modelo = Modelo.instance

    fun estaLogueado() = modelo.estaLogueado()

    fun registrarme(email: String, clave: String, listener: CallbackRegistro) {
        modelo.registrarUsuario(email, clave, listener)
    }

}