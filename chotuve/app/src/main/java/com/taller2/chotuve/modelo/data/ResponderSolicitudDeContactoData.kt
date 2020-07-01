package com.taller2.chotuve.modelo.data

import com.squareup.moshi.Json

enum class RespuestaSolicitudDeContacto(val value: String) {
    ACEPTAR("aceptar"),
    RECHAZAR("rechazar");
}

data class ResponderSolicitudDeContactoData(@field:Json(name = "accion") val accion: String)
