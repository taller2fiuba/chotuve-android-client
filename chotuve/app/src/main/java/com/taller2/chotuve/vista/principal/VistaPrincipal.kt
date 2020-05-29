package com.taller2.chotuve.vista.principal

import com.taller2.chotuve.modelo.Video

interface VistaPrincipal {
    fun mostrarVideos(videos: List<Video>)
    fun setErrorRed()
}