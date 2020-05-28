package com.taller2.chotuve.vista.adaptadores


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Video
import com.taller2.chotuve.vista.componentes.VideoPortadaConTituloYAutor

class VideosAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var videos: MutableList<Video> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val videoView: View = inflater.inflate(R.layout.video_portada_con_titulo_y_autor, parent, false)
        return VideoPortadaConTituloYAutor(videoView)
    }

    override fun onBindViewHolder(
        viewHolder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val video: Video = videos[position]
        val portadaVideo = viewHolder as VideoPortadaConTituloYAutor
        portadaVideo.setVideo(video)
    }

    override fun getItemCount(): Int {
        return videos.size
    }

    fun getItem(position: Int): Video {
        return videos[position]
    }

    fun add(video: Video) {
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
            remove(getItem(0))
        }
    }

    fun isEmpty(): Boolean {
        return itemCount == 0
    }
}