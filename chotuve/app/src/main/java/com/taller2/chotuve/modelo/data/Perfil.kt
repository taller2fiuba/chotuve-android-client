package com.taller2.chotuve.modelo.data

import com.squareup.moshi.Json

data class Perfil(@field:Json(name = "nombre") val nombre: String,
                  @field:Json(name = "apellido") val apellido: String,
                  @field:Json(name = "telefono") val telefono: String? = null,
                  @field:Json(name = "direccion") val direccion: String? = null,
                  @field:Json(name = "foto") val foto: String? = null)
