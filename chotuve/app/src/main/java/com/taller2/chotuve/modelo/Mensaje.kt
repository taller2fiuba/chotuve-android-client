package com.taller2.chotuve.modelo

import com.taller2.chotuve.util.obtenerFechaDeTimestamp

class Mensaje {
    var enviadoPor: Long? = null
    var mensaje: String? = null
    var timestamp: Long? = null
        set(value) {
            field = value
            fechaYHora = obtenerFechaDeTimestamp(value!!)
        }
    var fechaYHora: String? = null

    constructor() {} // Firebase requiere un constructor sin parametros
}