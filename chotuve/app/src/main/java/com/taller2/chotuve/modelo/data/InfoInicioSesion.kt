package com.taller2.chotuve.modelo.data

import com.squareup.moshi.Json

data class InfoInicioSesion(@field:Json(name = "email") val email: String,
                            @field:Json(name = "password") val clave: String)