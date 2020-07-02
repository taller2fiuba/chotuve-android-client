package com.taller2.chotuve.vista.scroll_infinito

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Video
import com.taller2.chotuve.vista.componentes.VideoPortadaConTituloYAutor
import com.taller2.chotuve.vista.principal.MuroDeVideosFragment


class VideosAdapter(val fragment: MuroDeVideosFragment) : ViewHolderAdapter() {
    private val VIDEO = 0
    private val CARGANDO = 1

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
                viewHolder =
                    CargandoViewHolder(
                        viewLoading
                    )
            }
        }
        return viewHolder!!
    }

    override fun onBindViewHolder(
        viewHolder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val video: Video? = elementos[position] as Video?

        if (getItemViewType(position) == VIDEO) {
            val portadaVideo = viewHolder as VideoPortadaConTituloYAutor
            portadaVideo.setVideo(video!!)

            portadaVideo.setOnClicklistener(object : VideoPortadaConTituloYAutor.Clicklistener {
                override fun onItemClick(view: View?, position: Int) {
                    fragment.verVideo((getItem(position)!! as Video).id)
                }
            })
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == elementos.size - 1 && estaCargando) CARGANDO else VIDEO
    }
}