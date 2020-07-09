package com.taller2.chotuve.vista.scroll_infinito

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Chat
import com.taller2.chotuve.vista.chats.ChatsFragment
import com.taller2.chotuve.vista.componentes.ChatViewHolder

class ChatsAdapter(val fragment: ChatsFragment, opciones: FirebaseRecyclerOptions<Chat>) : FirebaseRecyclerAdapter<Chat, ChatViewHolder>(opciones) {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ChatViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        return ChatViewHolder(
            inflater.inflate(
                R.layout.componente_chat,
                viewGroup,
                false
            )
        )
    }

    override fun onBindViewHolder(
        viewHolder: ChatViewHolder,
        position: Int,
        chat: Chat
    ) {
        fragment.chatsCargados()
        viewHolder.setChat(chat)
        viewHolder.clickListener = object : ChatViewHolder.Clicklistener {
            override fun onItemClick(view: View?, position: Int) {
                val chat = getItem(position)
                fragment.verMensajes(chat.key!!, chat.destinatario!!)
            }
        }
    }
}