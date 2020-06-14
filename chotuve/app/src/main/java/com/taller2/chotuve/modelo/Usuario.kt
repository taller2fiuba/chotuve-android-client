package com.taller2.chotuve.modelo

import android.net.Uri

class Usuario(val id: Long, val nombre: String, val apellido: String, val email: String, val telefono: String, val direccion: String, fotoPerfil: String) {
    val fotoPerfilUri: Uri = Uri.parse(fotoPerfil)
}