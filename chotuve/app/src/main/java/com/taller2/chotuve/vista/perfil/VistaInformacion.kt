package com.taller2.chotuve.vista.perfil

import com.taller2.chotuve.modelo.PerfilDeUsuario

interface VistaInformacion {
    fun mostrarPerfil(perfilDeUsuario: PerfilDeUsuario)
    fun setErrorRed()
}