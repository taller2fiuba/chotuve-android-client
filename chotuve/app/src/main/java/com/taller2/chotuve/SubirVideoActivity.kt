package com.taller2.chotuve

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SubirVideoActivity : AppCompatActivity() {
    lateinit var mStorage : StorageReference
    var uri = null as Uri?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.subir_video)

        mStorage = FirebaseStorage.getInstance().reference;
    }

    fun clickElegirVideo(view: View) {
        val intent = Intent()
        intent.setType("video/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Seleccionar video"), 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            uri = data!!.data
            var botonElegir = findViewById<View>(R.id.botonElegir) as Button
            botonElegir.visibility = View.GONE;
            val txtUriElegida = findViewById<View>(R.id.txtUriElegida) as TextView
            txtUriElegida.text = getFilenameFromUri(uri!!)
        }
    }

    private fun getFilenameFromUri(uri: Uri) : String {
        // https://stackoverflow.com/questions/5568874/how-to-extract-the-file-name-from-uri-returned-from-intent-action-get-content
        var result = null as String?
        if (uri.scheme.equals("content")) {
            val cursor = getContentResolver().query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor!!.close()
            }
        }

        if (result == null) {
            result = uri.getPath();
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }

    fun clickSubirVideo(view: View) {
        // TODO mejorar validaciones: sacar errores luego de solucionados, tener en cuenta errores devueltos por el server, usar alguna libreria (EasyValidation por ej)
        var titulo = findViewById<View>(R.id.titulo) as TextInputLayout
        var tituloString = titulo.editText!!.text.toString()
        if (tituloString == "") {
            titulo.error = "No puede estar vacio"
            return;
        }
        if (uri == null) {
            var botonElegir = findViewById<View>(R.id.botonElegir) as Button
            botonElegir.isFocusableInTouchMode = true
            botonElegir.requestFocus()
            botonElegir.error = "Elegí un video"
            return;
        }
        var mUri = uri!!
        var mReference = mStorage.child(getFilenameFromUri(mUri))
        val txtUrlDescarga = findViewById<View>(R.id.txtUrlDescarga) as TextView
        try {
            // La subida del video asincrónica
            mReference.putFile(mUri)
                .addOnSuccessListener { taskSnapshot ->
                    var context = this
                    // Pedir la url de descarga del video recien subido también es asincrónico (wtf?)
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener { downloadUri: Uri? ->
                        val call = AppServerService.create().crearVideo(Video(tituloString, downloadUri.toString()))
                        call.enqueue(object : Callback<ResponseBody> {
                            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                                val responseCode = response!!.code()!!
                                if (responseCode == 201) {
                                    txtUrlDescarga.text = ""
                                    Toast.makeText(context, "Subida Exitosa :)", Toast.LENGTH_LONG).show()
                                } else {
                                    // TODO: Manejar errores desde el app server.
                                    Log.d("retrofit", response.errorBody()!!.string())
                                    Toast.makeText(context, "Error del server", Toast.LENGTH_LONG).show()
                                }
                            }
                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                            }
                        })
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                }
            txtUrlDescarga.text = "Espere. Cargando video..."
        } catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }
    }
}
