package com.taller2.chotuve.vista.scrollinfinito

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Comentario
import com.taller2.chotuve.vista.ver_video.VerVideoActivity


class ComentariosAdapter(val activity: VerVideoActivity) : ViewHolderAdapter() {
    private val COMENTARIO = 0
    private val CARGANDO = 1

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        var viewHolder: RecyclerView.ViewHolder? = null

        when (viewType) {
            COMENTARIO -> {
                val viewItem: View =
                    inflater.inflate(R.layout.comentario, parent, false)
                viewHolder = ComentarioViewHolder(viewItem)
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
        val comentario: Comentario? = elementos[position] as Comentario?

        if (getItemViewType(position) == COMENTARIO) {
            val comentarioViewHolder = viewHolder as ComentarioViewHolder
            comentarioViewHolder.setComentario(comentario!!)

            comentarioViewHolder.setOnClicklistener(object : ComentarioViewHolder.ClickListener {
                override fun onItemClick(view: View?, position: Int) {
                    activity.irAPerfilDeUsuario((getItem(position)!! as Comentario).autor.usuarioId)
                }
            })
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == elementos.size - 1 && estaCargando) CARGANDO else COMENTARIO
    }
}