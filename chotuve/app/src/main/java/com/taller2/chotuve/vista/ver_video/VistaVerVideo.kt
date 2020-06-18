package com.taller2.chotuve.vista.ver_video

import com.taller2.chotuve.modelo.Comentario
import com.taller2.chotuve.modelo.Video

interface VistaVerVideo {
    fun mostrarVideo(video: Video)
    fun mostrarComentarios(comentarios: List<Comentario>)
    fun agregarNuevoComentario(comentario: String)
    fun setErrorRed()
}