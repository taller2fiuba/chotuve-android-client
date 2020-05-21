package com.taller2.chotuve

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.icu.lang.UCharacter.GraphemeClusterBreak
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
import kotlinx.android.synthetic.main.registro_de_usuario.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // si hay token guardado (ya estoy logeado) hacer intent a subir video
        setContentView(R.layout.registro_de_usuario)
    }

    fun clickRegistrarse(view: View) {
        var email = (findViewById<View>(R.id.email) as TextInputLayout)
        var contraseña = (findViewById<View>(R.id.contraseña) as TextInputLayout)
        var repetirContraseña = (findViewById<View>(R.id.repetir_contraseña) as TextInputLayout)
        var emailTexto = email.editText!!.text.toString()
        var contraseñaTexto = contraseña.editText!!.text.toString()
        var repetirContraseñaTexto = repetirContraseña.editText!!.text.toString()
        // TODO codigo de valdiacion repetido con titulo en subir video, mejorar validaciones
        if (emailTexto == "") {
            email.error = "No puede estar vacio"
            return;
        }
        if (contraseñaTexto == "") {
            contraseña.error = "No puede estar vacio"
            return;
        }
        if (repetirContraseñaTexto == "") {
            repetirContraseña.error = "No puede estar vacio"
            return;
        }
        if (repetirContraseñaTexto != contraseñaTexto) {
            repetirContraseña.error = "No era igual a la contraseña"
            repetirContraseña.editText!!.setText("")
            return;
        }
        // registrarme en el app server
        // guardar token de respuesta
        startActivity(Intent(this, SubirVideoActivity::class.java))
    }

    fun clickIniciarSesion(view: View) {
        startActivity(Intent(this, IniciarSesionActivity::class.java))
    }
}
