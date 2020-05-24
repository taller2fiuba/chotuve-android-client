package com.taller2.chotuve.vista.autenticacion

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.taller2.chotuve.vista.MainActivity
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.CallbackInicioSesion
import com.taller2.chotuve.modelo.Modelo
import com.taller2.chotuve.modelo.interactor.InteractorIniciarSesion
import com.taller2.chotuve.presentador.PresentadorIniciarSesion
import com.taller2.chotuve.vista.perfil.SubirVideoActivity
import kotlinx.android.synthetic.main.iniciar_sesion.*

class IniciarSesionActivity : AppCompatActivity(), VistaIniciarSesion {
    private val presentador = PresentadorIniciarSesion(this, InteractorIniciarSesion())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.iniciar_sesion)

        presentador.onCreate()
    }

    fun clickIniciarSesion(view: View) {
        val usuario = email.editText?.text.toString()
        val clave = contraseña.editText?.text.toString()
        when {
            usuario.isNullOrEmpty() -> email.error = "No puede estar vacío"
            clave.isNullOrEmpty() -> contraseña.error = "No puede estar vacío"
            else -> presentador.iniciarSesion(usuario, clave)
        }
    }

    fun clickRegistrarme(view: View) {
        startActivity(Intent(this, RegistrarmeActivity::class.java))
    }

    override fun onDestroy() {
        super.onDestroy()
        presentador.onDestroy()
    }

    override fun setUsuarioInexistente() {
        Toast.makeText(this, "No se encontró el usuario. ¿Registrarse?", Toast.LENGTH_LONG).show()
    }

    override fun setUsuarioOClaveIncorrecta() {
        Toast.makeText(this, "Usuario o e-mail incorrecto", Toast.LENGTH_LONG).show()
    }

    override fun setErrorRed() {
        Toast.makeText(this, "Se produjo un error de red, intente nuevamente más tarde", Toast.LENGTH_LONG).show()
    }

    override fun irAPantallaPrincipal() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}
