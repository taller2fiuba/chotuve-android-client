package com.taller2.chotuve.modelo.data

import com.squareup.moshi.Json

data class NuevoMensajeData(@field:Json(name = "mensaje") val mensaje: String)
