package com.taller2.chotuve.presentador

import com.taller2.chotuve.modelo.Video
import com.taller2.chotuve.modelo.interactor.InteractorVideos
import com.taller2.chotuve.modelo.interactor.desearilizador.DeserializadorUsuarioMuroDeVideos
import com.taller2.chotuve.vista.principal.VistaVideos

class PresentadorPrincipal (private val vista: VistaVideos) : PresentadorVideos {

    private val interactor: InteractorVideos = InteractorVideos(DeserializadorUsuarioMuroDeVideos())

    override fun obtenerVideos(pagina: Int) {
        interactor.obtenerVideos(pagina, object : InteractorVideos.CallbackObtenerVideo {
            override fun onObtenerExitoso(videos: List<Video>) {
                vista.mostrarVideos(videos)
            }
            override fun onErrorRed(mensaje: String?) {
                vista.setErrorRed()
            }
        })
    }
}