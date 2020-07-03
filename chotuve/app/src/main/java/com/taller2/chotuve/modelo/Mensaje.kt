package com.taller2.chotuve.modelo

//class Mensaje(val usuarioId: Long, val contenido: String, val fechaYHora: String) {
//    var id: String? = null
//}

class Mensaje {
    var id: String? = null
    var enviadoPor: Long? = null
    var mensaje: String? = null
    var fechaYHora: String? = null

    constructor() {} // Firebase requiere un constructor sin parametros
    constructor(id: String?, enviadoPor: Long?, mensaje: String?, fechaYHora: String?) {
        this.id = id
        this.enviadoPor = enviadoPor
        this.mensaje = mensaje
        this.fechaYHora = fechaYHora
    }

}