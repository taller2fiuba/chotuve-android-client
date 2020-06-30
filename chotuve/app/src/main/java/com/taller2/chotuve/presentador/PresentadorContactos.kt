package com.taller2.chotuve.presentador

import android.os.Handler
import com.taller2.chotuve.modelo.Autor
import com.taller2.chotuve.vista.perfil.VistaContactos

class PresentadorContactos(private val vista: VistaContactos) {

    fun obtenerContactos() {
        val handler = Handler()
        handler.postDelayed(Runnable {
            // TODO ver si los conectatos van a venir con foto de perfil o no
            val contactos = listOf<Autor>(
                Autor(1, "perrito"),
                Autor(2, "gatito")
            )
            vista.mostrarContactos(contactos)
        }, 1000)
    }
}