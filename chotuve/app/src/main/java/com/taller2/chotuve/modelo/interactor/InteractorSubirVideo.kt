package com.taller2.chotuve.modelo.interactor

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.taller2.chotuve.modelo.CallbackCrearVideo
import com.taller2.chotuve.modelo.Modelo
import com.taller2.chotuve.util.obtenerNombreDeArchivo

class InteractorSubirVideo {
    interface CallbackSubirVideo {
        fun onSubidaExitosa()
        fun onErrorSubida()
        fun onActualizarProgreso(progreso: Int)
    }
    interface CallbackCrearVideo {
        fun onExito()
        fun onError()
        fun onErrorRed()
    }

    private val modelo = Modelo.instance
    private val firebaseStorage = FirebaseStorage.getInstance().reference
    private lateinit var urlDescargaVideo: String

    fun subirVideoAFirebase(uri: Uri, callbackSubirVideo: CallbackSubirVideo) {
        var fileReference = firebaseStorage.child(obtenerNombreDeArchivo(uri))

        fileReference.putFile(uri)
            .addOnSuccessListener { taskSnapshot ->
                // Pedir la url de descarga del video recien subido también es asincrónico (wtf?)
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { downloadUri: Uri? ->
                    urlDescargaVideo = downloadUri.toString()
                    callbackSubirVideo.onSubidaExitosa()
                }
            }
            .addOnProgressListener { taskSnapshot ->
                val progress = (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
                callbackSubirVideo.onActualizarProgreso(progress.toInt())
            }
            .addOnFailureListener { e ->
                callbackSubirVideo.onErrorSubida()
            }
    }

    fun crearVideo(titulo: String, callbackCrearVideo: CallbackCrearVideo) {
        modelo.crearVideo(titulo, urlDescargaVideo, object : com.taller2.chotuve.modelo.CallbackCrearVideo {
            override fun onExito(url: String) {
                callbackCrearVideo.onExito()
            }

            override fun onError() {
                callbackCrearVideo.onError()
            }

            override fun onErrorRed(mensaje: String?) {
                callbackCrearVideo.onErrorRed()
            }
        })
    }


}