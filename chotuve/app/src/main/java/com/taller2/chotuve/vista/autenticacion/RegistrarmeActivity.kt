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
import com.taller2.chotuve.modelo.interactor.InteractorRegistrarme
import com.taller2.chotuve.presentador.PresentadorRegistrarme
import kotlinx.android.synthetic.main.registro_de_usuario.*


class RegistrarmeActivity : AppCompatActivity(), VistaRegistrarme {
    private val presentador = PresentadorRegistrarme(this, InteractorRegistrarme())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro_de_usuario)

        presentador.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        presentador.onDestroy()
    }

    fun clickRegistrarse(view: View) {
        val usuario = email.editText?.text?.toString()
        val clave = contraseña.editText?.text?.toString()
        val repetirClave = repetir_contraseña.editText?.text?.toString()

        when {
            usuario.isNullOrEmpty() -> email.error = "No puede estar vacío"
            clave.isNullOrEmpty() -> contraseña.error = "No puede estar vacío"
            clave != repetirClave -> repetir_contraseña.error = "Las contraseñas no coinciden"
            else -> presentador.registrarme(usuario, clave)
        }
    }

    fun clickIniciarSesion(view: View) {
        startActivity(Intent(this, IniciarSesionActivity::class.java))
    }

    override fun setUsuarioYaExiste() {
        email.error = "Este e-mail ya está en uso."
    }

    override fun setErrorRed() {
        Toast.makeText(this,
            "Se produjo un error de red. Intente nuevamente más tarde.",
            Toast.LENGTH_LONG).show()
    }

    override fun irAPantallaPrincipal() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}
