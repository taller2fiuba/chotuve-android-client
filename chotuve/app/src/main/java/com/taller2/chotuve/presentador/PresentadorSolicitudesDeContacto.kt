package com.taller2.chotuve.presentador

import com.taller2.chotuve.modelo.SolicitudDeContacto
import com.taller2.chotuve.modelo.interactor.InteractorContactos
import com.taller2.chotuve.vista.contactos.VistaSolicitudesDeContacto

class PresentadorSolicitudesDeContacto(private val vista: VistaSolicitudesDeContacto) {
    private val interactor = InteractorContactos()

    fun obtenerSolicitudes() {
        interactor.obtenerSolicitudesDeContacto(object : InteractorContactos.CallbackObtenerSolicitudesDeContacto {
            override fun onObtenerExitoso(solicitudes: List<SolicitudDeContacto>) {
                vista.mostrarSolicitudes(solicitudes)
            }
            override fun onErrorRed(mensaje: String?) {
                vista.setErrorRed()
            }
        })
    }

    fun rechazarSolicitud(solicitudId: Long) {
    }

    fun aceptarSolicitud(solicitudId: Long) {
    }
}