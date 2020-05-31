package com.taller2.chotuve.modelo

interface CallbackCrearVideo {
    /**
     * Se ejecutará si la subida fue exitosa, indicando la URL de descarga
     */
    fun onExito(url: String)

    /**
     * Se ejecutará si el server responde con algun error, por ahora solo devuelve 400 sin mensajes de error.
     */
    fun onError()

    /**
     * Se ejecutará si se produce algún error de red.
     */
    fun onErrorRed(mensaje: String?)
}