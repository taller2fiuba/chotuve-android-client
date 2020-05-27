package com.taller2.chotuve.vista.adaptadores


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Video
import com.taller2.chotuve.vista.componentes.VideoPortadaConInformacion


// Note that we specify the custom ViewHolder which gives us access to our views
class VideosAdapter : RecyclerView.Adapter<VideosAdapter.ViewHolder>() {
    private var videos: MutableList<Video> = mutableListOf()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var videoPortada: VideoPortadaConInformacion = itemView.findViewById<View>(R.id.video_portada_con_informacion) as VideoPortadaConInformacion
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val videoView: View = inflater.inflate(R.layout.video_portada_con_informacion_list, parent, false)
        return ViewHolder(videoView)
    }

    override fun onBindViewHolder(
        viewHolder: ViewHolder,
        position: Int
    ) {
        val video: Video = videos[position]
        val videoPortada = viewHolder.videoPortada
        videoPortada.setVideo(video)
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