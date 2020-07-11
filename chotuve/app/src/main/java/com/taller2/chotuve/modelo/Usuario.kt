package com.taller2.chotuve.modelo

import android.net.Uri

class Usuario(val id: Long, val email: String, fotoDePerfil: String?) {
    val fotoPerfilUri: Uri? = if (fotoDePerfil != null) Uri.parse(fotoDePerfil)
    else null
}