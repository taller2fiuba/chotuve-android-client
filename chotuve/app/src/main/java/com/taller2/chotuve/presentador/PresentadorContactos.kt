package com.taller2.chotuve.presentador

import android.os.Handler
import com.taller2.chotuve.modelo.Usuario
import com.taller2.chotuve.vista.contactos.VistaContactos

class PresentadorContactos(private val vista: VistaContactos) {

    fun obtenerContactos() {
        val handler = Handler()
        handler.postDelayed(Runnable {
            // TODO ver si los conectatos van a venir con foto de perfil o no
            val contactos = listOf<Usuario>(
                Usuario(1, "perrito"),
                Usuario(2, "gatito")
            )
            vista.mostrarContactos(contactos)
        }, 1000)
    }
}