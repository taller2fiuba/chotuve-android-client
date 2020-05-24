package com.taller2.chotuve.vista.autenticacion

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.taller2.chotuve.vista.MainActivity
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.CallbackRegistro
import com.taller2.chotuve.modelo.Modelo


class RegistrarmeActivity : AppCompatActivity() {
    private val modelo = Modelo.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // si hay token guardado
        if (modelo.estaLogueado())
            startActivity(Intent(this, MainActivity::class.java))
        else
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
        var contexto = this
        modelo.registrarUsuario(emailTexto, contraseñaTexto, object : CallbackRegistro {
            override fun onExito() {
                startActivity(Intent(contexto, MainActivity::class.java))
            }

            override fun onYaEstaRegistrado() {
                // Ya está registrado
                email.error = "Este e-mail ya está en uso."
            }

            override fun onErrorRed(mensaje: String?) {
                // Reintentar
                Toast.makeText(contexto, "Error de red. Reintente. $mensaje", Toast.LENGTH_LONG).show()
            }
        })

    }

    fun clickIniciarSesion(view: View) {
        startActivity(Intent(this, IniciarSesionActivity::class.java))
    }
}
