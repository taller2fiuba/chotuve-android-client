package com.taller2.chotuve.presentador

import android.util.Log
import com.taller2.chotuve.modelo.PerfilDeUsuario
import com.taller2.chotuve.modelo.Video
import com.taller2.chotuve.modelo.interactor.InteractorPerfil
import com.taller2.chotuve.modelo.interactor.InteractorVideos
import com.taller2.chotuve.modelo.interactor.desearilizador.DeserializadorUsuarioVideosDeUnUsuario
import com.taller2.chotuve.vista.principal.VistaVideos

class PresentadorVideosDeUnUsuario(private val usuarioId: Long, private var vista: VistaVideos?) : PresentadorVideos {
    private val interactorPerfil = InteractorPerfil()
    private var interactor: InteractorVideos? = null

    override fun obtenerVideos(pagina: Int) {
        if (interactor == null) {
            configurarInteractor(pagina)
        } else {
            obtenerVideosInterno(pagina)
        }
    }

    override fun onDestroy() {
        vista = null
    }

    private fun configurarInteractor(pagina: Int) {
        interactorPerfil.cargarPerfil(usuarioId, object : InteractorPerfil.CallbackCargarPerfil {
            override fun onExito(perfilDeUsuario: PerfilDeUsuario) {
                interactor = InteractorVideos(DeserializadorUsuarioVideosDeUnUsuario(perfilDeUsuario.usuario))
                obtenerVideosInterno(pagina)
            }

            override fun onError() {
                Log.d("P/Videos de un usuario", "Error obteniendo perfil")
                vista?.setErrorRed()
            }

            override fun onErrorRed() {
                Log.d("P/Videos de un usuario", "Error de red")
                vista?.setErrorRed()
            }
        })
    }

    private fun obtenerVideosInterno(pagina: Int) {
        interactor!!.obtenerVideosDeUsuario(usuarioId, pagina, object : InteractorVideos.CallbackObtenerVideo {
            override fun onObtenerExitoso(videos: List<Video>) {
                vista?.mostrarVideos(videos)
            }
            override fun onErrorRed(mensaje: String?) {
                vista?.setErrorRed()
            }
        })
    }
}