package com.taller2.chotuve

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.taller2.chotuve.modelo.CallbackInicioSesion
import com.taller2.chotuve.modelo.Modelo

class IniciarSesionActivity : AppCompatActivity() {
    private val modelo = Modelo.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (modelo.estaLogueado()) {
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
        modelo.iniciarSesion(emailTexto, contraseñaTexto, object : CallbackInicioSesion {
            override fun onExito() {
                startActivity(Intent(context, MainActivity::class.java))
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
        startActivity(Intent(this, RegistrarmeActivity::class.java))
    }
}
