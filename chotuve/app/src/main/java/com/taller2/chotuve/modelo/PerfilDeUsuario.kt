package com.taller2.chotuve.modelo

import android.net.Uri

enum class EstadoContacto(val value: String) {
    ES_CONTACTO("contacto"),
    SOLICITUD_ENVIADA("solicitud-enviada"),
    SOLICITUD_PENDIENTE("solicitud-pendiente");

    companion object {
        fun getByValue(value: String) = values().firstOrNull { it.value == value }
    }
}

class PerfilDeUsuario(val usuario: Usuario, val nombre: String?, val apellido: String?, val telefono: String?, val direccion: String?, val fotoPerfil: String?, val estadoContacto: EstadoContacto?) {
    val fotoPerfilUri: Uri?
        get() {
            if (fotoPerfil != null)
                return Uri.parse(fotoPerfil)
            return null
        }
}