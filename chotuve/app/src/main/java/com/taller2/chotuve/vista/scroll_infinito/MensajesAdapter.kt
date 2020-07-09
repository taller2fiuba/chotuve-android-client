package com.taller2.chotuve.vista.scroll_infinito

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Chat
import com.taller2.chotuve.modelo.Mensaje
import com.taller2.chotuve.vista.chats.ChatsFragment
import com.taller2.chotuve.vista.chats.MensajesFragment
import com.taller2.chotuve.vista.componentes.ChatViewHolder
import com.taller2.chotuve.vista.componentes.MensajeViewHolder

class MensajesAdapter(val fragment: MensajesFragment, val miUsuarioId: Long, opciones: FirebaseRecyclerOptions<Mensaje>) : FirebaseRecyclerAdapter<Mensaje, MensajeViewHolder>(opciones) {
    private val MENSAJE_MIO = 0
    private val MENSAJE_OTRO = 1

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MensajeViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        var viewHolder: MensajeViewHolder? = null
        when (viewType) {
            MENSAJE_MIO -> {
                val viewMensajeMio: View =
                    inflater.inflate(R.layout.componente_mensaje_mio, viewGroup, false)
                viewHolder =  MensajeViewHolder(viewMensajeMio)
            }
            MENSAJE_OTRO -> {
                val viewMensajeOtro: View =
                    inflater.inflate(R.layout.componente_mensaje_otro, viewGroup, false)
                viewHolder = MensajeViewHolder(viewMensajeOtro)
            }
        }
        return viewHolder!!
    }

    override fun onBindViewHolder(
        viewHolder: MensajeViewHolder,
        position: Int,
        mensaje: Mensaje
    ) {
        fragment.mensajesCargados()
        viewHolder.setMensaje(mensaje)
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).enviadoPor == miUsuarioId) MENSAJE_MIO else MENSAJE_OTRO
    }
}