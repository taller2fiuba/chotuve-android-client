package com.taller2.chotuve.modelo.interactor

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.taller2.chotuve.modelo.Modelo

class InteractorPrincipal {
    interface CallbackObtenerVideo {
        fun onObtenerExitoso(uri: Uri, duracion: Long, titulo: String, autor: String, creacion: String)
        fun onErrorRed(mensaje: String?)
    }

    private val modelo = Modelo.instance
    private val firebaseStorage = FirebaseStorage.getInstance()

    fun obtenerVideo(callback: CallbackObtenerVideo) {
        // TODO LUCHO pedir al modelo los videos a mostrar
        val videoDownloadUrl = "https://firebasestorage.googleapis.com/v0/b/chotuve-a8587.appspot.com/o/videos%2FPeople%20Waiting.mp4?alt=media&token=76524658-e3c6-4ebb-a98c-240ff86bb8d4"
        val uri = Uri.parse(videoDownloadUrl)
        val titulo = "el titulo"
        val autor = "yo papa"
        val creacion = "28/02/2018"
        var fileReference = firebaseStorage.getReferenceFromUrl(videoDownloadUrl)

        fileReference.metadata.addOnSuccessListener { metadata: StorageMetadata ->
            callback.onObtenerExitoso(uri,
                metadata.getCustomMetadata("duracion")!!.toLong(), titulo, autor, creacion)
        }.addOnFailureListener { e ->
            callback.onErrorRed(e.message)
        }
    }
}