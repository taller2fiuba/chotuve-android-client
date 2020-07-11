package com.taller2.chotuve.vista.principal

import com.taller2.chotuve.modelo.Video

interface VistaVideos {
    fun mostrarVideos(videos: List<Video>)
    fun setErrorRed()
    fun verVideo(id: String)
}