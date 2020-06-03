package com.taller2.chotuve.presentador

import android.os.Handler
import com.taller2.chotuve.modelo.Video
import com.taller2.chotuve.modelo.interactor.InteractorPrincipal
import com.taller2.chotuve.modelo.interactor.InteractorVerVideo
import com.taller2.chotuve.vista.principal.VistaPrincipal
import com.taller2.chotuve.vista.ver_video.VistaVerVideo

class PresentadorVerVideo (private val vista: VistaVerVideo,
                           private val interactor: InteractorVerVideo
) {
    fun onDestroy() {
    }

    fun obtenerVideo(id: String) {
        // TODO LUCHO llamar al interactor
        // TODO eliminar esto, solo tarda un segundo para ver el cargando
        Handler().postDelayed(
            {
                val videoDownloadUrl = "https://firebasestorage.googleapis.com/v0/b/chotuve-a8587.appspot.com/o/videos%2FPeople%20Waiting.mp4?alt=media&token=76524658-e3c6-4ebb-a98c-240ff86bb8d4"
                vista.mostrarVideo(Video(videoDownloadUrl, "1", "el titulo", "yo papa", "28/02/2018", 20))
            },
            1000 // value in milliseconds
        )
    }
}