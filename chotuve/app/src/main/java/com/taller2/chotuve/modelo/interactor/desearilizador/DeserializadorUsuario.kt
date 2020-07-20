package com.taller2.chotuve.modelo.interactor.desearilizador

import com.taller2.chotuve.modelo.Usuario
import com.taller2.chotuve.util.deserializarUsuario
import org.json.JSONObject

interface DeserializadorUsuario {
    fun deserializar(objeto: JSONObject) : Usuario
}

class DeserializadorUsuarioMuroDeVideos : DeserializadorUsuario {
    override fun deserializar(objeto: JSONObject) : Usuario {
        val autorJson = objeto.getJSONObject("autor")
        return deserializarUsuario(autorJson)
    }
}

class DeserializadorUsuarioVideosDeUnUsuario(val usuario: Usuario) : DeserializadorUsuario {
    override fun deserializar(objeto: JSONObject) : Usuario {
        return usuario
    }
}