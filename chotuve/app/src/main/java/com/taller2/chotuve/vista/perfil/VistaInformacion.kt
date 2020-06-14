package com.taller2.chotuve.vista.perfil

import com.taller2.chotuve.modelo.Usuario

interface VistaInformacion {
    fun mostrarPerfil(usuario: Usuario)
    fun setErrorRed()
}