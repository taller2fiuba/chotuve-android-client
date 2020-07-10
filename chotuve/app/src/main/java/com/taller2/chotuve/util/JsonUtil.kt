package com.taller2.chotuve.util

import com.taller2.chotuve.modelo.Usuario
import org.json.JSONObject

fun getString(data: JSONObject, campo: String): String? {
    if (data.has(campo)) {
        val valor = data.getString(campo)
        if (valor != "null" && valor != "") {
            return valor
        }
    }
    return null
}

fun deserializarUsuarioId(json: JSONObject) : Usuario{
    return deserializarUsuarioInterno(json, "id")
}

fun deserializarUsuario(json: JSONObject) : Usuario{
    return deserializarUsuarioInterno(json, "usuario_id")
}

private fun deserializarUsuarioInterno(json: JSONObject, idKey: String) : Usuario{
    return Usuario(
        json.getLong(idKey),
        json.getString("email"),
        getString(json, "foto")
    )
}