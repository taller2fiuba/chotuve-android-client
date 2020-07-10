package com.taller2.chotuve.vista.componentes

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Video
import com.taller2.chotuve.util.cargarImagen


class VideoPortadaConTituloYAutor(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var portada: VideoPortada = itemView.findViewById<View>(R.id.video_portada) as VideoPortada
    private var titulo: TextView = itemView.findViewById<View>(R.id.titulo) as TextView
    private var autorYCreacion: TextView = itemView.findViewById<View>(R.id.autor_y_creacion) as TextView
    private var usuarioFotoPerfil: ImageView = itemView.findViewById<View>(R.id.usuario_foto_de_perfil) as ImageView
    var id: String? = null
    var clickListener: Clicklistener? = null

    init {
        itemView.setOnClickListener { view ->
            clickListener!!.onItemClick(view, adapterPosition)
        }
    }

    interface Clicklistener {
        fun onItemClick(view: View?, position: Int)
    }

    fun setVideo(video: Video) {
        id = video.id
        portada.setUri(video.uri, video.duracion)
        titulo.text = video.titulo
        // TODO pasar a formato hace X años/meses/semanas/horas/minutos
        autorYCreacion.text = itemView.context.getString(R.string.autor_y_creacion, video.autor.email, video.creacion)
        cargarImagen(video.autor, usuarioFotoPerfil, itemView)
    }
}