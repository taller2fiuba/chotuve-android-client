package com.taller2.chotuve.vista.autenticacion

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.interactor.InteractorRegistrarme
import com.taller2.chotuve.presentador.PresentadorRegistrarme
import kotlinx.android.synthetic.main.registro_de_usuario.*


class RegistrarmeActivity : IniciarSesionGoogleActivity(), VistaRegistrarme {
    private val presentador = PresentadorRegistrarme(this, InteractorRegistrarme())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro_de_usuario)

        presentador.onCreate()
    }

    fun clickRegistrarse(view: View) {
        email.error = ""
        contraseña.error = ""
        repetir_contraseña.error = ""

        val emailString = email.editText?.text?.toString()
        val clave = contraseña.editText?.text?.toString()
        val repetirClave = repetir_contraseña.editText?.text?.toString()

        when {
            emailString.isNullOrEmpty() -> email.error = "No puede estar vacío"
            !Patterns.EMAIL_ADDRESS.matcher(emailString).matches() -> email.error = "Debe ser un email válido"
            clave.isNullOrEmpty() -> contraseña.error = "No puede estar vacío"
            clave != repetirClave -> repetir_contraseña.error = "Las contraseñas no coinciden"
            else -> {
                btnSiguiente.isEnabled = false
                presentador.registrarme(emailString, clave)
            }
        }
    }

    fun clickIniciarSesion(view: View) {
        startActivity(Intent(this, IniciarSesionActivity::class.java))
    }

    override fun setUsuarioYaExiste() {
        btnSiguiente.isEnabled = true
        email.error = "Este email ya está en uso."
    }
}
