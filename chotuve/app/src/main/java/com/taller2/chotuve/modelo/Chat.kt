package com.taller2.chotuve.modelo

import android.util.Log
import com.taller2.chotuve.util.obtenerFechaDeTimestamp

class Chat {
    var key: String? = null
    var ultimoMensaje: String? = null
    var timestamp: Long? = null
        set(value) {
            field = value
            fechaYHora = obtenerFechaDeTimestamp(value!!)
        }
    var fechaYHora: String? = null
    var destinatario: Usuario? = null

    constructor() {} // Firebase requiere un constructor sin parametros
}