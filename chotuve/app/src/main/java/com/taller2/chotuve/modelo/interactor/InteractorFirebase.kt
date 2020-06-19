package com.taller2.chotuve.modelo.interactor

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.taller2.chotuve.modelo.Modelo
import com.taller2.chotuve.util.obtenerDuracionVideo
import com.taller2.chotuve.util.obtenerNombreDeArchivo

class InteractorFirebase(val carpeta: String) {
    interface CallbackSubir {
        fun onSubidaExitosa()
        fun onErrorSubida()
        fun onActualizarProgreso(progreso: Int)
    }

    private val firebaseStorage = FirebaseStorage.getInstance().reference
    var urlDescarga: String? = null

    fun subir(uri: Uri, callbackSubirAFirebase: CallbackSubir) {
        val fileReference = firebaseStorage.child(carpeta + "/" + obtenerNombreDeArchivo(uri))

        fileReference.putFile(uri)
            .addOnSuccessListener { taskSnapshot ->
                // Pedir la url de descarga del video recien subido también es asincrónico (wtf?)
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { downloadUri: Uri? ->
                    urlDescarga = downloadUri.toString()
                    callbackSubirAFirebase.onSubidaExitosa()
                }
            }
            .addOnProgressListener { taskSnapshot ->
                val progress = (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
                callbackSubirAFirebase.onActualizarProgreso(progress.toInt())
            }
            .addOnFailureListener { e ->
                callbackSubirAFirebase.onErrorSubida()
            }
    }
}