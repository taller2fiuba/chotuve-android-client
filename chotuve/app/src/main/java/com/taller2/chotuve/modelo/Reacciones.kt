package com.taller2.chotuve.modelo

enum class Reaccion(val value: String) {
    ME_GUSTA("me-gusta"),
    NO_ME_GUSTA("no-me-gusta");

    companion object {
        fun getByValue(value: String) = values().firstOrNull { it.value == value }
    }
}

class Reacciones(val meGustas: Long, val noMeGustas: Long, var miReaccion: Reaccion?)