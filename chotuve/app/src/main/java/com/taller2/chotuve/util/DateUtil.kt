package com.taller2.chotuve.util

import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat

fun obtenerFechaDeIso8601(iso8601: String) : String {
    val parser: DateTimeFormatter = ISODateTimeFormat.dateTimeParser()
    val fecha = parser.parseDateTime(iso8601)
    return formatearFechaSegunDia(fecha.toLocalDateTime())
}

fun obtenerFechaDeTimestamp(timestamp: Long, negado: Boolean = false) : String {
    var timestampInterno = timestamp
    if (negado) {
        timestampInterno = -timestamp
    }
    val fecha = LocalDateTime(timestampInterno * 1000L)
    return formatearFechaSegunDia(fecha)
}

fun formatearFechaSegunDia(fecha: LocalDateTime): String {
    val hoy = LocalDate()
    if (fecha.toLocalDate().compareTo(hoy) == 0) {
        return DateTimeFormat.forPattern("h:mm a").print(fecha)
    }
    return DateTimeFormat.forPattern("dd/MM/yy").print(fecha)
}