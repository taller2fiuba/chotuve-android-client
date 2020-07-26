package com.taller2.chotuve.presentador

import com.taller2.chotuve.modelo.interactor.InteractorCambiarClave
import com.taller2.chotuve.vista.perfil.VistaCambiarClave


class PresentadorCambiarClave(private val vista: VistaCambiarClave) {
    private val interactorCambiarClave = InteractorCambiarClave()

    fun cambiarClave(email: String, claveActual: String, nuevaClave: String) {
        interactorCambiarClave.cambiarClave(
            email,
            claveActual,
            nuevaClave,
            object : InteractorCambiarClave.CallbackCambiarClave {
                override fun onExito() {
                    vista.onExito()
                }

                override fun onErrorRed() {
                    vista.setErrorRed()
                }

                override fun onError() {
                    vista.setError()
                }
            }
        )
    }
}