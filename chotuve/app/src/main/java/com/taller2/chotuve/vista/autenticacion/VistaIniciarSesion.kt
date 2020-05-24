package com.taller2.chotuve.vista.autenticacion

interface VistaIniciarSesion {
    fun setUsuarioInexistente()
    fun setUsuarioOClaveIncorrecta()
    fun setErrorRed()
    fun irAPantallaPrincipal()
}