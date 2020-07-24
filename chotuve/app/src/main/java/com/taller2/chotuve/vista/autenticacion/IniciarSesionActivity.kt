package com.taller2.chotuve.vista.autenticacion

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.interactor.InteractorIniciarSesion
import com.taller2.chotuve.presentador.PresentadorIniciarSesion
import kotlinx.android.synthetic.main.iniciar_sesion.*


class IniciarSesionActivity : IniciarSesionGoogleActivity(), VistaIniciarSesion {
    private val presentador = PresentadorIniciarSesion(this, InteractorIniciarSesion())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.iniciar_sesion)

        presentador.onCreate()
    }

    fun clickIniciarSesion(view: View) {
        email.error = ""
        contraseña.error = ""

        val usuario = email.editText?.text?.toString()
        val clave = contraseña.editText?.text?.toString()
        when {
            usuario.isNullOrEmpty() -> email.error = "No puede estar vacío"
            clave.isNullOrEmpty() -> contraseña.error = "No puede estar vacío"
            else -> {
                btnSiguiente.isEnabled = false
                presentador.iniciarSesion(usuario, clave)
            }
        }
    }

    fun clickRegistrarme(view: View) {
        startActivity(Intent(this, RegistrarmeActivity::class.java))
    }

    override fun setUsuarioOClaveIncorrecta() {
        btnSiguiente.isEnabled = true
        Toast.makeText(this, "Usuario o e-mail incorrecto", Toast.LENGTH_LONG).show()
    }
}
