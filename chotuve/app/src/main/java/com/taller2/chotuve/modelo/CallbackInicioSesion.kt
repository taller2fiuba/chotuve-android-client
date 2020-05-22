package com.taller2.chotuve.modelo

interface CallbackInicioSesion {
    /**
     * Se ejecutará al tener un inicio de sesión correcto.
     */
    fun onExito()

    /**
     * Se ejecutará si el e-mail no estaba registrado.
     */
    fun onEmailNoRegistrado()

    /**
     * Se ejecutará si la combinación del e-mail/clave no coincide con la de ningún
     * usuario.
     */
    fun onUsuarioOClaveIncorrecta()

    /**
     * Se ejecutará si se produce algún error de red.
     */
    fun onErrorRed(mensaje: String?)
}
