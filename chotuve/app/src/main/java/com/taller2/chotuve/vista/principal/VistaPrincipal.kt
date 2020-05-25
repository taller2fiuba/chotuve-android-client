package com.taller2.chotuve.vista.principal

import android.net.Uri

interface VistaPrincipal {
    fun mostrarCargandoVideo()
    fun mostrarVideo(uri: Uri, duracion: Long, titulo: String, autor: String, creacion: String)
    fun setErrorRed()
}