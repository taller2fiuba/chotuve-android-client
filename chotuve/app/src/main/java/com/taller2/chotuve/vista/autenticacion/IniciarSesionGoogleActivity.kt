package com.taller2.chotuve.vista.autenticacion

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.taller2.chotuve.presentador.PresentadorGoogle
import com.taller2.chotuve.vista.MainActivity


abstract class IniciarSesionGoogleActivity : AppCompatActivity(), VistaGoogle {
    private val presentador = PresentadorGoogle(this)

    fun clickIngresarConGoogle(view: View) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestId()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent: Intent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, 0)
    }

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

    override fun setErrorRed() {
        Toast.makeText(this, "Se produjo un error de red, intente nuevamente más tarde", Toast.LENGTH_LONG).show()
    }

    override fun irAPantallaPrincipal() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    override fun setErrorGoogle() {
        Toast.makeText(this, "Se produjo validando tu cuenta de Google", Toast.LENGTH_LONG).show()
    }
}
