package com.taller2.chotuve

import android.content.Intent
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.taller2.chotuve.modelo.CallbackSubirVideo
import com.taller2.chotuve.modelo.Modelo
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class SubirVideoActivity : AppCompatActivity() {
    private val modelo = Modelo.instance
    private lateinit var mStorage : StorageReference
    private var uri = null as Uri?
    private lateinit var urlDescargaVideo : String
    private lateinit var botonCrearVideo : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mStorage = FirebaseStorage.getInstance().reference;

        val intent = Intent()
        intent.type = "video/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Seleccionar video"), 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            setContentView(R.layout.subir_video)
            botonCrearVideo = findViewById<View>(R.id.boton_crear) as Button
            botonCrearVideo.isEnabled = false

            uri = data!!.data
            val portadaVideo = findViewById<View>(R.id.portada_video) as ImageView
            Glide.with(this).load(uri!!).into(portadaVideo)

            val duracionTextView = findViewById<View>(R.id.duracion) as TextView
            val duracion = obtenerDuracion(uri!!)
            duracionTextView.text = duracion

            subirVideoAFirebase(uri!!)
        }
    }

    private fun obtenerNombreDeArchivo(uri: Uri) : String {
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

    fun clickCrearVideo(view: View) {
        // TODO mejorar validaciones: sacar errores luego de solucionados, tener en cuenta errores devueltos por el server, usar alguna libreria (EasyValidation por ej)
        var titulo = findViewById<View>(R.id.titulo) as TextInputLayout
        var tituloString = titulo.editText!!.text.toString()
        if (tituloString == "") {
            titulo.error = "No puede estar vacio"
            return;
        }

        var context = this
        var spinnerCreando = findViewById<View>(R.id.creando_video_barra_progreso) as ProgressBar
        spinnerCreando.visibility = View.VISIBLE
        botonCrearVideo.isEnabled = false
        modelo.subirVideo(tituloString, urlDescargaVideo, object : CallbackSubirVideo {
            override fun onExito(url: String) {
                Log.d("vista", "Subido con éxito: terminando actividad")
                finish()
            }

            override fun onErrorRed(mensaje: String?) {
                Log.d("vista", "Error del server ($mensaje)")
                spinnerCreando.visibility = View.INVISIBLE
                botonCrearVideo.isEnabled = true
                Toast.makeText(context, "Error del server ($mensaje)", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun subirVideoAFirebase(uri: Uri) {
        var mReference = mStorage.child(obtenerNombreDeArchivo(uri))
        try {
            val barraProgreso = findViewById<View>(R.id.subir_video_progress_bar) as ProgressBar
            // La subida del video asincrónica
            mReference.putFile(uri)
                .addOnSuccessListener { taskSnapshot ->
                    // Pedir la url de descarga del video recien subido también es asincrónico (wtf?)
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener { downloadUri: Uri? ->
                        urlDescargaVideo = downloadUri.toString()
                        botonCrearVideo.isEnabled = true
                    }
                }
                .addOnProgressListener { taskSnapshot ->
                    val progress = (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
                    barraProgreso.progress = progress.toInt();
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                }
        } catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun obtenerDuracion(videoUri: Uri) : String {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(this, videoUri)
        val duracion = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toLong() / 1000
        val duracionString = DateUtils.formatElapsedTime(duracion)

        retriever.release()
        return duracionString
    }
}
