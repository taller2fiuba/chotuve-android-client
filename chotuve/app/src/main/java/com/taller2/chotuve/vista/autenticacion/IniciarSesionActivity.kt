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
import com.taller2.chotuve.modelo.interactor.InteractorIniciarSesion
import com.taller2.chotuve.presentador.PresentadorIniciarSesion
import com.taller2.chotuve.vista.MainActivity
import kotlinx.android.synthetic.main.iniciar_sesion.*


class IniciarSesionActivity : IniciarSesionGoogleActivity(), VistaIniciarSesion {
    private val presentador = PresentadorIniciarSesion(this, InteractorIniciarSesion())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.iniciar_sesion)

        presentador.onCreate()
    }

    fun clickIniciarSesion(view: View) {
        val usuario = email.editText?.text?.toString()
        val clave = contraseña.editText?.text?.toString()
        when {
            usuario.isNullOrEmpty() -> email.error = "No puede estar vacío"
            clave.isNullOrEmpty() -> contraseña.error = "No puede estar vacío"
            else -> presentador.iniciarSesion(usuario, clave)
        }
    }

    fun clickRegistrarme(view: View) {
        startActivity(Intent(this, RegistrarmeActivity::class.java))
    }

    override fun setUsuarioOClaveIncorrecta() {
        Toast.makeText(this, "Usuario o e-mail incorrecto", Toast.LENGTH_LONG).show()
    }
}
