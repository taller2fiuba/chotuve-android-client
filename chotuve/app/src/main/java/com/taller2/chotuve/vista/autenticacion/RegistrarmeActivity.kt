package com.taller2.chotuve.vista.autenticacion

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import androidx.core.content.ContextCompat
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.interactor.InteractorRegistrarme
import com.taller2.chotuve.presentador.PresentadorRegistrarme
import kotlinx.android.synthetic.main.registro_de_usuario.*
import java.util.*


class RegistrarmeActivity : IniciarSesionGoogleActivity(), VistaRegistrarme {
    private val presentador = PresentadorRegistrarme(this, InteractorRegistrarme())
    private lateinit var cambioEnTextoWatcher: TextWatcher
    private val TEXTOS_FUERZA = mapOf(
        -1 to "Demasiado larga",
        0 to "No ingresada",
        1 to "Muy Baja",
        2 to "Baja",
        3 to "Aceptable",
        4 to "Alta",
        5 to "Muy alta"
    )

    private val COLOR_FUERZA = mapOf(
        -1 to android.R.color.holo_red_dark,
        0 to android.R.color.darker_gray,
        1 to android.R.color.holo_red_dark,
        2 to android.R.color.holo_orange_dark,
        3 to android.R.color.holo_orange_light,
        4 to android.R.color.holo_green_light,
        5 to android.R.color.holo_green_dark
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro_de_usuario)

        configurarCambioEnTextoWatcher()
        contraseña.editText!!.addTextChangedListener(cambioEnTextoWatcher)
        presentador.onCreate()
    }

    fun configurarCambioEnTextoWatcher() {
        cambioEnTextoWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {}

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {}

            override fun afterTextChanged(s: Editable) {
                setearFuerzaClave(s.toString())
            }
        }
    }

    fun setearFuerzaClave(clave: String) {
        val fuerza = getFuerzaClave(clave)

        indicador_fuerza_clave_texto.text = TEXTOS_FUERZA[fuerza]
        indicador_fuerza_clave_progreso.progressTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this,
                COLOR_FUERZA[fuerza] ?: android.R.color.holo_red_dark
            ))
        indicador_fuerza_clave_progreso.progress = fuerza
    }

    private fun getFuerzaClave(clave: String): Int {
        var fuerza = 0
        if (clave.isNotEmpty()) {
            fuerza++
        }

        if (clave.length > 5) {
            fuerza++
        }

        if (clave.toLowerCase(Locale.ROOT) !== clave) {
            fuerza++
        }

        if (clave.length > 8) {
            fuerza++
        }

        val cantidadDigitos: Int = clave.filter { it.isDigit() }.length
        if (cantidadDigitos > 0 && cantidadDigitos != clave.length) {
            fuerza++
        }
        if (clave.length > 40) {
            fuerza = -1
        }
        return fuerza
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
            clave.length > 40 -> contraseña.error = "No puede tener mas de 40 carácteres"
            getFuerzaClave(clave) < 3 -> contraseña.error = "Debe ser mas fuerte: usa numeros, mayusculas y minisculas"
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
