package com.taller2.chotuve.modelo

interface CallbackRegistro {
    /**
     * Se ejecutará si el registro fue exitoso.
     */
    public fun onExito();

    /**
     * Se ejecutará si el e-mail ya estaba registrado.
     */
    public fun onYaEstaRegistrado();

    /**
     * Se ejecutará si se produce algún error de red.
     */
    public fun onErrorRed(mensaje: String?);
}