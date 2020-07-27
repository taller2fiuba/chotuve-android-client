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

        clave_view.setHint("contraseña")
        clave_view.boton = btnSiguiente
        presentador.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        presentador.onDestroy()
    }


    fun clickRegistrarse(view: View) {
        email.error = ""
        clave_view.setError("")
        repetir_contraseña.error = ""

        val emailString = email.editText?.text?.toString()
        val repetirClave = repetir_contraseña.editText?.text?.toString()

        when {
            emailString.isNullOrEmpty() -> email.error = "No puede estar vacío"
            !Patterns.EMAIL_ADDRESS.matcher(emailString).matches() -> email.error = "Debe ser un email válido"
            clave_view.estaVacio() -> clave_view.setError("No puede estar vacío")
            clave_view.superaLargoMaximo() -> clave_view.setError("No puede tener mas de 40 carácteres")
            !clave_view.tieneFuerzaAceptable() -> clave_view.setError("Debe ser mas fuerte: usa numeros, mayusculas y minisculas")
            !clave_view.coincideCon(repetirClave!!) -> repetir_contraseña.error = "Las contraseñas no coinciden"
            else -> {
                btnSiguiente.isEnabled = false
                presentador.registrarme(emailString, clave_view.getClave())
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
