package com.taller2.chotuve.vista.contactos

import com.taller2.chotuve.modelo.Autor

interface VistaContactos {
    fun mostrarContactos(contactos: List<Autor>)
    fun setErrorRed()
}