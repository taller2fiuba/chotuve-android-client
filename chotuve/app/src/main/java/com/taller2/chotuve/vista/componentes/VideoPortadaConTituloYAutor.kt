package com.taller2.chotuve.vista.componentes

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Video

class VideoPortadaConTituloYAutor(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var portada: VideoPortada = itemView.findViewById<View>(R.id.video_portada) as VideoPortada
    private var titulo: TextView = itemView.findViewById<View>(R.id.titulo) as TextView
    private var autorYCreacion: TextView = itemView.findViewById<View>(R.id.autor_y_creacion) as TextView

    fun setVideo(video: Video) {
        portada.setUri(video.uri, video.duracion)
        titulo.text = video.titulo
        autorYCreacion.text = itemView.context.getString(R.string.autor_y_creacion, video.autor, video.creacion)
        // TODO pasar a formato hace X años/meses/semanas/horas/minutos
    }
}