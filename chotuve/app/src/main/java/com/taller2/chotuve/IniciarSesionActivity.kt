package com.taller2.chotuve

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout

class IniciarSesionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // si hay token guardado y algo me trajo aca tendria que eliminar ese token
        setContentView(R.layout.iniciar_sesion)
    }

    fun clickIniciarSesion(view: View) {
        var email = (findViewById<View>(R.id.email) as TextInputLayout)
        var contraseña = (findViewById<View>(R.id.contraseña) as TextInputLayout)
        var emailTexto = email.editText!!.text.toString()
        var contraseñaTexto = contraseña.editText!!.text.toString()
        // TODO codigo de validacion repetido, mejorar validaciones
        if (emailTexto == "") {
            email.error = "No puede estar vacio"
            return;
        }
        if (contraseñaTexto == "") {
            contraseña.error = "No puede estar vacio"
            return;
        }
        // logearme en el app server
        // guardar token de respuesta
        startActivity(Intent(this, MainActivity::class.java))
    }

    fun clickRegistrarme(view: View) {
        startActivity(Intent(this, RegistrarmeActivity::class.java))
    }
}
