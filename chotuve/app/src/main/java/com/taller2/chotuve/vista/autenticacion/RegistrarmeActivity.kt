package com.taller2.chotuve.vista.autenticacion

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.interactor.InteractorRegistrarme
import com.taller2.chotuve.presentador.PresentadorRegistrarme
import com.taller2.chotuve.vista.MainActivity
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
        email.error = "Este email ya está en uso."
    }
}
