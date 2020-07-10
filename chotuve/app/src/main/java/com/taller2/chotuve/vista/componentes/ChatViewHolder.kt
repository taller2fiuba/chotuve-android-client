package com.taller2.chotuve.vista.componentes

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Chat
import com.taller2.chotuve.util.cargarImagen


class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var usuarioFotoPerfil: ImageView = itemView.findViewById<View>(R.id.destinatario_foto_de_perfil) as ImageView
    private var usuarioEmail: TextView = itemView.findViewById<View>(R.id.chat_usuario) as TextView
    private var ultimoMensaje: TextView = itemView.findViewById<View>(R.id.chat_ultimo_mensaje) as TextView
    private var fechaYHoraUltimoMensaje: TextView = itemView.findViewById<View>(R.id.fecha_y_hora_ultimo_mensaje) as TextView
    var clickListener: Clicklistener? = null

    // TODO codigo repetido con VideoPortadoConTituloYAutor
    init {
        itemView.setOnClickListener { view ->
            clickListener!!.onItemClick(view, adapterPosition)
        }
    }

    interface Clicklistener {
        fun onItemClick(view: View?, position: Int)
    }

    fun setChat(chat: Chat) {
        usuarioEmail.text = chat.destinatario!!.email
        cargarImagen(chat.destinatario!!, usuarioFotoPerfil, itemView)
        ultimoMensaje.text =  chat.ultimoMensaje
        fechaYHoraUltimoMensaje.text = chat.fechaYHora
    }
}