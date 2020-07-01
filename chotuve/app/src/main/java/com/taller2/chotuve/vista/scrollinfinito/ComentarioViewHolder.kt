package com.taller2.chotuve.vista.scrollinfinito

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Comentario


class ComentarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var autor: TextView = itemView.findViewById<View>(R.id.autor) as TextView
    private var fecha: TextView = itemView.findViewById<View>(R.id.fecha) as TextView
    private var textoComentario: TextView = itemView.findViewById<View>(R.id.texto_comentario) as TextView
    var usuarioId: Long? = null
    private var mClickListener: ClickListener? = null

    // TODO clickListener codigo repetido con portada
    init {
        itemView.setOnClickListener { view ->
            mClickListener!!.onItemClick(view, adapterPosition)
        }
    }

    interface ClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    fun setOnClicklistener(clickListener: ClickListener?) {
        mClickListener = clickListener
    }

    fun setComentario(comentario: Comentario) {
        usuarioId = comentario.autor.id
        autor.text = comentario.autor.email
        fecha.text = comentario.fecha
        textoComentario.text = comentario.comentario
    }
}