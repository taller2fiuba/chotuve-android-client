package com.taller2.chotuve.vista.adaptadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Video
import com.taller2.chotuve.vista.componentes.CargandoViewHolder
import com.taller2.chotuve.vista.componentes.VideoPortadaConTituloYAutor

class VideosAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIDEO = 0
    private val CARGANDO = 1

    private var videos: MutableList<Video?> = mutableListOf()
    private var estaCargando = false

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        var viewHolder: RecyclerView.ViewHolder? = null

        when (viewType) {
            VIDEO -> {
                val viewItem: View =
                    inflater.inflate(R.layout.video_portada_con_titulo_y_autor, parent, false)
                viewHolder = VideoPortadaConTituloYAutor(viewItem)
            }
            CARGANDO -> {
                val viewLoading: View =
                    inflater.inflate(R.layout.cargando_view_holder, parent, false)
                viewHolder = CargandoViewHolder(viewLoading)
            }
        }
        return viewHolder!!
    }

    override fun onBindViewHolder(
        viewHolder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val video: Video? = videos[position]

        if (getItemViewType(position) == VIDEO) {
            val portadaVideo = viewHolder as VideoPortadaConTituloYAutor
            portadaVideo.setVideo(video!!)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == videos.size - 1 && estaCargando) CARGANDO else VIDEO
    }

    fun agregarCargando() {
        estaCargando = true
        add(null)
    }

    fun sacarCargando() {
        if (estaCargando) {
            estaCargando = false
            val position: Int = videos.size - 1
            videos.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int {
        return videos.size
    }

    fun getItem(position: Int): Video? {
        return videos[position]
    }

    fun add(video: Video?) {
        videos.add(video)
        notifyItemInserted(videos.size - 1)
    }

    fun addAll(videos: List<Video>) {
        for (video in videos) {
            add(video)
        }
    }

    fun remove(video: Video) {
        val position: Int = videos.indexOf(video)
        if (position > -1) {
            videos.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clear() {
        while (itemCount > 0) {
            remove(getItem(0)!!)
        }
    }

    fun isEmpty(): Boolean {
        return itemCount == 0
    }
}