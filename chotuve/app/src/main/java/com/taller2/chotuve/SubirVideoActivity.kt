package com.taller2.chotuve

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.taller2.chotuve.modelo.CallbackSubirVideo
import com.taller2.chotuve.modelo.Modelo


class SubirVideoActivity : AppCompatActivity() {
    private val modelo = Modelo.instance
    private lateinit var mStorage : StorageReference
    private var uri = null as Uri?

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

            uri = data!!.data
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
        var mUri = uri!!
        var mReference = mStorage.child(modelo.getFilenameFromUri(this, mUri))
        val txtUrlDescarga = findViewById<View>(R.id.txtUrlDescarga) as TextView
        txtUrlDescarga.text = "Espere. Cargando video..."
        val context = this
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
