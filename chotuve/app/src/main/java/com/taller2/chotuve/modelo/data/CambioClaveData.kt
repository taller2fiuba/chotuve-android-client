package com.taller2.chotuve.modelo.data

import com.squareup.moshi.Json

data class CambioClaveData(@field:Json(name = "clave_actual") val claveActual: String, @field:Json(name = "nueva_clave") val nuevaClave: String)
