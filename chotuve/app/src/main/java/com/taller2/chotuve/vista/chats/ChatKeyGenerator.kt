package com.taller2.chotuve.vista.chats

import com.taller2.chotuve.modelo.Modelo

class ChatKeyGenerator {
    private val miUsuarioId = Modelo.instance.id

    companion object {
        val instance = ChatKeyGenerator()
    }

    fun generarKey(usuarioId: Long): String {
        return if (usuarioId > miUsuarioId!!) {
            "$miUsuarioId-$usuarioId"
        } else {
            "$usuarioId-$miUsuarioId"
        }
    }
}