package com.taller2.chotuve.modelo

import android.net.Uri

class PerfilDeUsuario(val usuario: Usuario, val nombre: String?, val apellido: String?, val telefono: String?, val direccion: String?, val fotoPerfil: String?) {
    val fotoPerfilUri: Uri?
        get() {
            if (fotoPerfil != null)
                return Uri.parse(fotoPerfil)
            return null
        }
}