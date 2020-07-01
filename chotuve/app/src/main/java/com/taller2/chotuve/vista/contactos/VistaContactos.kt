package com.taller2.chotuve.vista.contactos

import com.taller2.chotuve.modelo.Usuario

interface VistaContactos {
    fun mostrarContactos(contactos: List<Usuario>)
    fun setErrorRed()
}