package com.taller2.chotuve.vista.componentes

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Chat


class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var usuario: TextView = itemView.findViewById<View>(R.id.chat_usuario) as TextView
    private var ultimoMensaje: TextView = itemView.findViewById<View>(R.id.chat_ultimo_mensaje) as TextView
    private var fechaYHoraUltimoMensaje: TextView = itemView.findViewById<View>(R.id.fecha_y_hora_ultimo_mensaje) as TextView

    fun setChat(chat: Chat) {
        usuario.text = chat.key
        ultimoMensaje.text =  chat.ultimoMensaje
        fechaYHoraUltimoMensaje.text = chat.fechaYHora
    }
}