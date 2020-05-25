package com.taller2.chotuve.util

import android.net.Uri
import android.provider.OpenableColumns
import com.taller2.chotuve.Chotuve

/**
 * Devuelve el nombre de un archivo a partir de su URI.
 */
fun obtenerNombreDeArchivo(uri: Uri) : String {
    val contentResolver = Chotuve.context.applicationContext.contentResolver
    var result = null as String?
    if (uri.scheme.equals("content")) {
        val cursor = contentResolver.query(uri, null, null, null, null)
        try {
            if (cursor != null && cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        } finally {
            cursor!!.close()
        }
    }

    if (result == null) {
        result = uri.path
        val cut = result!!.lastIndexOf('/')
        if (cut != -1) result = result.substring(cut + 1)
    }
    return result
}