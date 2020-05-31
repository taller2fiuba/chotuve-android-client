package com.taller2.chotuve.modelo.data

import com.squareup.moshi.Json

data class Video(@field:Json(name = "titulo") val titulo: String,
                 @field:Json(name = "duracion") val duracion: Long,
                 @field:Json(name = "ubicacion") val ubicacion: String,
                 @field:Json(name = "descripcion") val descripcion: String? = "",
                 @field:Json(name = "visibilidad") val visibilidad: String,
                 @field:Json(name = "url") val url: String)
