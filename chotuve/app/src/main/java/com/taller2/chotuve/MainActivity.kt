package com.taller2.chotuve

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_main.*
import org.w3c.dom.Text

const val EXTRA_MESSAGE = "com.taller2.chotuve.MESSAGE"

class MainActivity : AppCompatActivity() {
    lateinit var mStorage : StorageReference
    var uri = null as Uri?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mStorage = FirebaseStorage.getInstance().reference;
    }

    private fun getFilenameFromUri(uri: Uri) : String {
        // https://stackoverflow.com/questions/5568874/how-to-extract-the-file-name-from-uri-returned-from-intent-action-get-content
        var result = null as String?
        if (uri.scheme.equals("content")) {
            val cursor = getContentResolver().query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor!!.close();
            }
        }

        if (result == null) {
            result = uri.getPath();
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            uri = data!!.data
            var botonElegir = findViewById<View>(R.id.botonElegir) as Button
            botonElegir.visibility = View.GONE;
            val txtUriElegida = findViewById<View>(R.id.txtUriElegida) as TextView
            txtUriElegida.text = getFilenameFromUri(uri!!)
        }
    }

    fun clickElegirVideo(view: View) {
        val intent = Intent()
        intent.setType("video/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Seleccionar video"), 0)
    }

    fun clickSubirVideo(view: View) {
        var titulo = findViewById<View>(R.id.titulo) as TextInputLayout
        if (titulo.editText!!.text.toString() == "") {
            titulo.error = "No puede estar vacio"
            return;
        }
        if (uri == null) {
            var botonElegir = findViewById<View>(R.id.botonElegir) as Button
            botonElegir.setFocusableInTouchMode(true)
            botonElegir.requestFocus()
            botonElegir.error = "Eligí un video"
            return;
        }
        var mUri = uri!!
        var mReference = mStorage.child(getFilenameFromUri(mUri))
        try {
            // La subida del video asincrónica
            mReference.putFile(mUri)
                .addOnSuccessListener { taskSnapshot ->
                    val txtUrlDescarga = findViewById<View>(R.id.txtUrlDescarga) as TextView

                    // Pedir la url de descarga del video recien subido también es asincrónico (wtf?)
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                        downloadUri: Uri? ->
                        txtUrlDescarga.text = downloadUri.toString()
                    }
                    Toast.makeText(this, "Subida Exitosa :)", Toast.LENGTH_LONG).show()
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
