package com.taller2.chotuve.util

import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat

fun obtenerFechaDeTimestamp(timestamp: Long, negado: Boolean = false) : String {
    var timestampInterno = timestamp
    if (negado) {
        timestampInterno = -timestamp
    }
    val fecha = LocalDateTime(timestampInterno * 1000L)
    val hoy = LocalDate()
    if (fecha.toLocalDate().compareTo(hoy) == 0) {
        return DateTimeFormat.forPattern("h:mm a").print(fecha)
    }
    return DateTimeFormat.forPattern("dd/MM/yy").print(fecha)
}