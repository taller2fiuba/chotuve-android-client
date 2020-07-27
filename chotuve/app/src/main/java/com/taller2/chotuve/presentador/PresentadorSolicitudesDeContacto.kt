package com.taller2.chotuve.presentador

import com.taller2.chotuve.modelo.SolicitudDeContacto
import com.taller2.chotuve.modelo.interactor.InteractorContactos
import com.taller2.chotuve.vista.contactos.VistaSolicitudesDeContacto

class PresentadorSolicitudesDeContacto(private var vista: VistaSolicitudesDeContacto?) {
    private val interactor = InteractorContactos()

    fun onDestroy() {
        vista = null
    }

    fun obtenerSolicitudes() {
        interactor.obtenerSolicitudesDeContacto(object : InteractorContactos.CallbackObtenerSolicitudesDeContacto {
            override fun onObtenerExitoso(solicitudes: List<SolicitudDeContacto>) {
                vista?.mostrarSolicitudes(solicitudes)
            }
            override fun onErrorRed(mensaje: String?) {
                vista?.setErrorRed()
            }
        })
    }

    fun rechazarSolicitud(solicitudId: Long) {
        interactor.rechazarSolicitud(solicitudId, object : InteractorContactos.CallbackResponderSolicitudesDeContacto {
            override fun onErrorRed(mensaje: String?) {
                vista?.setErrorRed()
            }
        })

    }

    fun aceptarSolicitud(solicitudId: Long) {
        interactor.aceptarSolicitud(solicitudId, object : InteractorContactos.CallbackResponderSolicitudesDeContacto {
            override fun onErrorRed(mensaje: String?) {
                vista?.setErrorRed()
            }
        })
    }
}