package com.taller2.chotuve.presentador

import android.os.Handler
import com.taller2.chotuve.modelo.Usuario
import com.taller2.chotuve.modelo.interactor.InteractorContactos
import com.taller2.chotuve.vista.contactos.VistaContactos

class PresentadorContactos(private val vista: VistaContactos) {
    private val interactor = InteractorContactos()

    fun obtenerContactos() {
        interactor.obtenerContactos(object : InteractorContactos.CallbackObtenerContactos {
            override fun onObtenerExitoso(contactos: List<Usuario>) {
                vista.mostrarContactos(contactos)
            }
            override fun onErrorRed(mensaje: String?) {
                vista.setErrorRed()
            }
        })
    }
}