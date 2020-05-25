package com.taller2.chotuve.modelo.interactor

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.taller2.chotuve.modelo.Modelo
import com.taller2.chotuve.util.obtenerNombreDeArchivo

class InteractorSubirVideo {
    interface CallbackSubirVideo {
        fun onSubidaExitosa()
        fun onErrorSubida()
        fun onActualizarProgreso(progreso: Int)
    }

    private val modelo = Modelo.instance
    private val firebaseStorage = FirebaseStorage.getInstance().reference
    private lateinit var urlDescargaVideo: String

    fun subirVideoAFirebase(uri: Uri, duracion: Long, callbackSubirVideo: CallbackSubirVideo) {
        var fileReference = firebaseStorage.child("videos/" + obtenerNombreDeArchivo(uri))
        var metadata = StorageMetadata.Builder()
            .setCustomMetadata("duracion", duracion.toString())
            .build()

        fileReference.putFile(uri, metadata)
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

    fun crearVideo(titulo: String, callbackSubirVideo: CallbackSubirVideo) {
        modelo.subirVideo(titulo, urlDescargaVideo, object : com.taller2.chotuve.modelo.CallbackSubirVideo {
            override fun onExito(url: String) {
                callbackSubirVideo.onSubidaExitosa()
            }

            override fun onErrorRed(mensaje: String?) {
                callbackSubirVideo.onErrorSubida()
            }
        })
    }


}