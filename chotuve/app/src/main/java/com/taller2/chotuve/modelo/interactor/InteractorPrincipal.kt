package com.taller2.chotuve.modelo.interactor

import android.net.Uri
import android.os.Handler
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.taller2.chotuve.modelo.Modelo
import com.taller2.chotuve.modelo.Video

class InteractorPrincipal {
    interface CallbackObtenerVideo {
        fun onObtenerExitoso(videos: List<Video>)
        fun onErrorRed(mensaje: String?)
    }

    private val modelo = Modelo.instance
    private val firebaseStorage = FirebaseStorage.getInstance()

    fun obtenerVideos(pagina: Int, callback: CallbackObtenerVideo) {
        // TODO LUCHO pedir al modelo los videos a mostrar
        // ojo que pagina es 0, 1, 2, 3... hay que multicarlo por la cantidad de elementos que devuelve el modelo
        // TODO eliminar esto, solo tarda un segundo para ver el cargando
        Handler().postDelayed(
            {
                var videosUrls = emptyList<String>()
                if (pagina == 0 || pagina == 1) {
                    videosUrls = listOf<String>(
                        "https://firebasestorage.googleapis.com/v0/b/chotuve-a8587.appspot.com/o/videos%2FPeople%20Waiting.mp4?alt=media&token=76524658-e3c6-4ebb-a98c-240ff86bb8d4",
                        "https://firebasestorage.googleapis.com/v0/b/chotuve-a8587.appspot.com/o/videos%2FLake%20Ripple.mp4?alt=media&token=c0672fa6-ff12-4da4-b6a3-fa2ef123027c",
                        "https://firebasestorage.googleapis.com/v0/b/chotuve-a8587.appspot.com/o/videos%2FPexels%20Videos%201194715.mp4?alt=media&token=1b3e6b16-9256-4c04-bb1a-d198fcb1a592",
                        "https://firebasestorage.googleapis.com/v0/b/chotuve-a8587.appspot.com/o/videos%2FPexels%20Videos%201572432.mp4?alt=media&token=8ba26f7a-e44c-43cb-931e-bbbb36622203",
                        "https://firebasestorage.googleapis.com/v0/b/chotuve-a8587.appspot.com/o/videos%2FPexels%20Videos%201856985.mp4?alt=media&token=88e89e46-35cb-4b21-8b11-ae92ffefa80e",
                        "https://firebasestorage.googleapis.com/v0/b/chotuve-a8587.appspot.com/o/videos%2FPeople%20Waiting.mp4?alt=media&token=76524658-e3c6-4ebb-a98c-240ff86bb8d4",
                        "https://firebasestorage.googleapis.com/v0/b/chotuve-a8587.appspot.com/o/videos%2FVID_20200506_143758.mp4?alt=media&token=9792d689-2600-480b-9263-05555a824da9",
                        "https://firebasestorage.googleapis.com/v0/b/chotuve-a8587.appspot.com/o/videos%2FVID_20200522_172834.mp4?alt=media&token=62f88036-c79c-4b17-a351-b1a56ef9df3a",
                        "https://firebasestorage.googleapis.com/v0/b/chotuve-a8587.appspot.com/o/videos%2Fvideo%20(1).mp4?alt=media&token=46732b20-59cf-4ef8-8761-da1f50835291",
                        "https://firebasestorage.googleapis.com/v0/b/chotuve-a8587.appspot.com/o/videos%2Fvideo%20(3).mp4?alt=media&token=fe20ea32-f45f-4845-8a59-909e934ec1c4"
                    )
                } else if (pagina == 2) {
                    videosUrls = listOf<String>(
                        "https://firebasestorage.googleapis.com/v0/b/chotuve-a8587.appspot.com/o/videos%2FPeople%20Waiting.mp4?alt=media&token=76524658-e3c6-4ebb-a98c-240ff86bb8d4",
                        "https://firebasestorage.googleapis.com/v0/b/chotuve-a8587.appspot.com/o/videos%2FLake%20Ripple.mp4?alt=media&token=c0672fa6-ff12-4da4-b6a3-fa2ef123027c",
                        "https://firebasestorage.googleapis.com/v0/b/chotuve-a8587.appspot.com/o/videos%2FPexels%20Videos%201194715.mp4?alt=media&token=1b3e6b16-9256-4c04-bb1a-d198fcb1a592",
                        "https://firebasestorage.googleapis.com/v0/b/chotuve-a8587.appspot.com/o/videos%2FPexels%20Videos%201572432.mp4?alt=media&token=8ba26f7a-e44c-43cb-931e-bbbb36622203"
                    )
                }

                callback.onObtenerExitoso(videosUrls.map { videoDownloadUrl ->
                    Video(videoDownloadUrl, "el titulo", "yo papa", "28/02/2018", 20)
                })
            },
            3000 // value in milliseconds
        )
    }
}