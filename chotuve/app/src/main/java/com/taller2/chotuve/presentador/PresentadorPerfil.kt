package com.taller2.chotuve.presentador

import android.os.Handler
import android.util.Log
import com.taller2.chotuve.modelo.Usuario
import com.taller2.chotuve.modelo.interactor.InteractorPerfil
import com.taller2.chotuve.vista.perfil.VistaInformacion


class PresentadorPerfil (private var vista: VistaInformacion?) {
    private val interactor = InteractorPerfil()

    fun onDestroy() {
        vista = null
    }

    fun obtenerInformacion(usuarioId: Long?) {
        val callback = object : InteractorPerfil.CallbackCargarPerfil {
            override fun onExito(usuario: Usuario) {
                vista?.mostrarPerfil(usuario)
            }

            override fun onError() {
                // ?
                Log.d("P/Perfil", "Error genérico")
            }

            override fun onErrorRed() {
                // ?
                Log.d("P/Perfil", "Error de red")
            }
        }

        if (usuarioId == null)
            interactor.cargarMiPerfil(callback)
        else
            interactor.cargarPerfil(usuarioId, callback)
    }
}