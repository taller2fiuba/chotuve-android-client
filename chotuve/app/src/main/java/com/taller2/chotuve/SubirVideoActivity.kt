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
import com.taller2.chotuve.modelo.AppServerService
import com.taller2.chotuve.modelo.CallbackSubirVideo
import com.taller2.chotuve.modelo.Modelo
import com.taller2.chotuve.modelo.data.Video
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SubirVideoActivity : AppCompatActivity() {
    val modelo = Modelo.instance
    var uri = null as Uri?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!modelo.estaLogueado()) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            setContentView(R.layout.subir_video)
        }
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
            txtUriElegida.text = modelo.getFilenameFromUri(this, uri!!)
        }
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
        val context = this
        val mUri = uri!!
        val txtUrlDescarga = findViewById<View>(R.id.txtUrlDescarga) as TextView
        txtUrlDescarga.text = "Espere. Cargando video..."
        modelo.subirVideo(this, tituloString, mUri, object : CallbackSubirVideo {
            override fun onExito(url: String) {
                txtUrlDescarga.text = url
                Toast.makeText(context, "Subida Exitosa :)", Toast.LENGTH_LONG).show()
            }

            override fun onErrorRed(mensaje: String?) {
                txtUrlDescarga.text = "Error"
                Toast.makeText(context, mensaje!!, Toast.LENGTH_LONG).show()
            }
        })
    }
}
