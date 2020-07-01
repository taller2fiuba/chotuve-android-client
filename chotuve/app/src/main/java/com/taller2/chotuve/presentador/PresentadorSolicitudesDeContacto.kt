package com.taller2.chotuve.presentador

import android.os.Handler
import com.taller2.chotuve.modelo.Usuario
import com.taller2.chotuve.modelo.SolicitudDeContacto
import com.taller2.chotuve.vista.contactos.VistaSolicitudesDeContacto

class PresentadorSolicitudesDeContacto(private val vista: VistaSolicitudesDeContacto) {

    fun obtenerSolicitudes() {
        val handler = Handler()
        handler.postDelayed(Runnable {
            val solicitudes = listOf<SolicitudDeContacto>(
                SolicitudDeContacto(1, Usuario(1, "perrito")),
                SolicitudDeContacto(2, Usuario(2, "gatito"))
            )
            vista.mostrarSolicitudes(solicitudes)
        }, 1000)
    }

    fun rechazarSolicitud(solicitudId: Long) {
    }

    fun aceptarSolicitud(solicitudId: Long) {
    }
}