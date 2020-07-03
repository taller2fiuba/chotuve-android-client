package com.taller2.chotuve.modelo

class Chat {
    var key: String? = null
    var ultimoMensaje: String? = null
    var fechaYHora: String? = null

    constructor() {} // Firebase requiere un constructor sin parametros
    constructor(key: String?, ultimoMensaje: String?, fechaYHora: String?) {
        this.key = key
        this.ultimoMensaje = ultimoMensaje
        this.fechaYHora = fechaYHora
    }
}