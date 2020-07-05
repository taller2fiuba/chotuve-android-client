package com.taller2.chotuve.util

import android.util.Log
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat

fun obtenerFechaDeTimestamp(timestamp: Long) : String {
    val fecha = LocalDateTime(timestamp * 1000L)
    val hoy = LocalDate()
    Log.d("hoy", hoy.toString())
    Log.d("fecha", fecha.toString())
    Log.d("fecha joda", LocalDate(timestamp * 1000L).toString())
    if (fecha.toLocalDate().compareTo(hoy) == 0) {
        return DateTimeFormat.forPattern("h:mm a").print(fecha)
    }
    return DateTimeFormat.forPattern("dd-MM-yyyy").print(fecha)
}