package com.taller2.chotuve.util

import android.media.MediaMetadataRetriever
import android.net.Uri
import com.taller2.chotuve.Chotuve

/**
 * Devuelve la duración de un video en milisegundos.
 */
fun obtenerDuracionVideo(uri: Uri) : Long {
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(Chotuve.context, uri)
    return retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toLong()
}