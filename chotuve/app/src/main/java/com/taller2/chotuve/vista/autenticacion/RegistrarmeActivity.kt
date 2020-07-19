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

    fun clickRegistrarseConGoogle(view: View) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestId()
                    .build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso);
        val signInIntent: Intent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val cuentaGoogle: GoogleSignInAccount = task.getResult(ApiException::class.java)!!
            presentador.registrarme(cuentaGoogle.email!!, cuentaGoogle.id!!)
        } catch (e: ApiException) {
            Toast.makeText(this, "Se produjo un error. Intente nuevamente más tarde.",
                Toast.LENGTH_LONG).show()
        }
    }

    override fun setUsuarioYaExiste() {
        // TODO ver que esto tambien se llama cuando el usuario de google ya existe tambien
        email.error = "Este e-mail ya está en uso."
    }

    override fun setErrorRed() {
        Toast.makeText(this,
            "Se produjo un error de red. Intente nuevamente más tarde.",
            Toast.LENGTH_LONG).show()
    }

    override fun irAPantallaPrincipal() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}
