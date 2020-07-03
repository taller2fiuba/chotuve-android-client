package com.taller2.chotuve.vista.contactos

import com.taller2.chotuve.modelo.SolicitudDeContacto

interface VistaSolicitudesDeContacto {
    fun mostrarSolicitudes(solicitudes: List<SolicitudDeContacto>)
    fun rechazarSolicitud(solicitudId: Long)
    fun aceptarSolicitud(solicitudId: Long)
    fun irAPerfilDeUsuario(usuarioId: Long)
    fun setErrorRed()
}