package com.taller2.chotuve.util

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