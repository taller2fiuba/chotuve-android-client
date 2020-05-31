package com.taller2.chotuve.modelo.data

data class Video(val titulo: String, val duracion: Long, val ubicacion: String, val descripcion: String? = "", val visibilidad: String, val url: String, val id: String  = "")