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


class IniciarSesionActivity : AppCompatActivity(), VistaIniciarSesion {
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

    fun clickIniciarSesionGoogle(view: View) {
        // TODO codigo repetido
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestId()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso);
        val signInIntent: Intent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, 0)
    }

    // TODO codigo repetido
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val cuentaGoogle: GoogleSignInAccount = task.getResult(ApiException::class.java)!!
            presentador.iniciarSesion(cuentaGoogle.email!!, cuentaGoogle.id!!)
        } catch (e: ApiException) {
            Toast.makeText(this, "Se produjo un error. Intente nuevamente más tarde.",
                Toast.LENGTH_LONG).show()
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
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}
