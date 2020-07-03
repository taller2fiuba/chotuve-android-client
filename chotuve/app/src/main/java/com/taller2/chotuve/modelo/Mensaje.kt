package com.taller2.chotuve.modelo

class Mensaje {
    var enviadoPor: Long? = null
    var mensaje: String? = null
    var fechaYHora: String? = null

    constructor() {} // Firebase requiere un constructor sin parametros
    constructor(enviadoPor: Long?, mensaje: String?, fechaYHora: String?) {
        this.enviadoPor = enviadoPor
        this.mensaje = mensaje
        this.fechaYHora = fechaYHora
    }
}