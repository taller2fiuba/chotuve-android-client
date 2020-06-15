package com.taller2.chotuve.presentador

import android.os.Handler
import com.taller2.chotuve.vista.perfil.VistaEditarInformacion


class PresentadorEditarPerfil (private val vista: VistaEditarInformacion) {
    fun onDestroy() {
    }

    fun editarPerfil(nombre: String, apellido: String, telefono: String?, direccion: String?) {
        val handler = Handler()
        handler.postDelayed(
            Runnable {  vista.onEdicionExitosa() },
            1000
        )
    }
}