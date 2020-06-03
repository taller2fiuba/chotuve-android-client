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
                val videoDownloadUrl = "https://firebasestorage.googleapis.com/v0/b/chotuve-a8587.appspot.com/o/videos%2Fy2mate.com%20-%20Denzel%20Curry%20-%20Ultimate%20(Full%20Video)_dT8dmvAzIqA_240p.mp4?alt=media&token=6adbe71b-02d9-4747-9763-d5d9333a62c2"
                vista.mostrarVideo(Video(videoDownloadUrl, "1", "musica epicarda", "libe de burzaco", "28/02/2018", 20))
            },
            1000 // value in milliseconds
        )
    }
}