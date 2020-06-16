package com.taller2.chotuve.modelo

enum class Reaccion(val reaccion: String) {
    ME_GUSTA("me-gusta"),
    NO_ME_GUSTA("no-me-gusta")
}

class Reacciones(val meGustas: Long, val noMeGustas: Long, var miReaccion: Reaccion?)