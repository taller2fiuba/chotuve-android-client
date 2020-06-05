package com.taller2.chotuve.modelo

import android.net.Uri

class Video(downloadUrl: String, val id: String, val titulo: String, val autor: String, val creacion: String, val descripcion: String, val duracion: Long) {
    val uri: Uri = Uri.parse(downloadUrl)
}