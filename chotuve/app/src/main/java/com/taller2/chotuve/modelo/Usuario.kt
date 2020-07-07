package com.taller2.chotuve.modelo

import android.net.Uri

// TODO sacar el null default cuando todos los usos tengan la foto
class Usuario(val id: Long, val email: String, fotoDePerfil: String? = null) {
    val fotoPerfilUri: Uri? = if (fotoDePerfil != null) Uri.parse(fotoDePerfil)
    else null
}