package com.taller2.chotuve.vista.componentes

import android.net.Uri
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Video


class VideoPortadaConTituloYAutor(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var portada: VideoPortada = itemView.findViewById<View>(R.id.video_portada) as VideoPortada
    private var titulo: TextView = itemView.findViewById<View>(R.id.titulo) as TextView
    private var autorYCreacion: TextView = itemView.findViewById<View>(R.id.autor_y_creacion) as TextView
    private var mClickListener: Clicklistener? = null

    init {
        itemView.setOnClickListener { view ->
            mClickListener!!.onItemClick(view, adapterPosition)
        }
    }

    interface Clicklistener {
        fun onItemClick(view: View?, position: Int)
    }

    fun setOnClicklistener(clicklistener: Clicklistener?) {
        mClickListener = clicklistener
    }

    fun setVideo(video: Video) {
        portada.setUri(video.uri, video.duracion)
        titulo.text = video.titulo
        // TODO pasar a formato hace X años/meses/semanas/horas/minutos
        autorYCreacion.text = itemView.context.getString(R.string.autor_y_creacion, video.autor, video.creacion)
    }

    fun getUri() : Uri {
        return portada.uri
    }
}