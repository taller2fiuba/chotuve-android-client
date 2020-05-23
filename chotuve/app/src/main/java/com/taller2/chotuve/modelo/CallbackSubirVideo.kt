package com.taller2.chotuve.modelo

interface CallbackSubirVideo {
    /**
     * Se ejecutará si la subida fue exitosa, indicando la URL de descarga
     */
    fun onExito(url: String)

    /**
     * Se ejecutará si se produce algún error de red.
     */
    fun onErrorRed(mensaje: String?)
}