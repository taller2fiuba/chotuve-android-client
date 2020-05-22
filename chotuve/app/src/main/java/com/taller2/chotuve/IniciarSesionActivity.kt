package com.taller2.chotuve

import android.annotation.SuppressLint
import android.content.Intent
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
import com.taller2.chotuve.modelo.CallbackInicioSesion
import com.taller2.chotuve.modelo.Modelo
import kotlinx.android.synthetic.main.registro_de_usuario.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class IniciarSesionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Modelo.instance.estaLogueado()) {
            startActivity(Intent(this, SubirVideoActivity::class.java))
        } else {
            // si hay token guardado y algo me trajo aca tendria que eliminar ese token
            setContentView(R.layout.iniciar_sesion)
        }
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
        val context = this
        // logearme en el app server
        Modelo.instance.iniciarSesion(emailTexto, contraseñaTexto, object : CallbackInicioSesion {
            override fun onExito() {
                startActivity(Intent(context, SubirVideoActivity::class.java))
            }

            override fun onUsuarioOClaveIncorrecta() {
                Toast.makeText(context, "Usuario o e-mail incorrecto", Toast.LENGTH_LONG).show()
            }

            override fun onEmailNoRegistrado() {
                Toast.makeText(context, "No se encontró el usuario. ¿Registrarse?", Toast.LENGTH_LONG).show()
            }

            override fun onErrorRed(mensaje: String?) {
                Toast.makeText(context, "Se produjo un error de red, intente nuevamente más tarde", Toast.LENGTH_LONG).show()
            }
        })

        // guardar token de respuesta [No hace falta, queda en el modelo]
    }

    fun clickRegistrarme(view: View) {
        startActivity(Intent(this, MainActivity::class.java))
    }
}
