package com.taller2.chotuve.vista.principal

import com.taller2.chotuve.modelo.Video

interface VistaPrincipal {
    fun mostrarCargandoVideo()
    fun mostrarVideo(video: Video)
    fun setErrorRed()
}