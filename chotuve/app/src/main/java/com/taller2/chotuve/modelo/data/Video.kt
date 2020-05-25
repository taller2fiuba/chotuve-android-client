package com.taller2.chotuve.modelo.data

import com.squareup.moshi.Json

data class Video(@field:Json(name = "titulo") val titulo: String,
                 @field:Json(name = "url") val url: String)