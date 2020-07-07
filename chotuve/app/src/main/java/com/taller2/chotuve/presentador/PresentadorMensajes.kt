package com.taller2.chotuve.presentador

import android.os.Handler
import com.taller2.chotuve.modelo.Usuario
import com.taller2.chotuve.modelo.interactor.InteractorContactos
import com.taller2.chotuve.modelo.interactor.InteractorMensajes
import com.taller2.chotuve.vista.chats.VistaMensajes
import com.taller2.chotuve.vista.contactos.VistaContactos

class PresentadorMensajes(private val vista: VistaMensajes) {
    private val interactor = InteractorMensajes()

    fun enviarMensaje(destinarioId: Long, mensaje: String) {
        interactor.enviarMensaje(destinarioId, mensaje, object : InteractorMensajes.CallbackEnviarMensaje {
            override fun onObtenerExitoso() {
                vista.mensajeEnviado()
            }
            override fun onErrorRed(mensaje: String?) {
                vista.setErrorRed()
            }
        })
    }
}