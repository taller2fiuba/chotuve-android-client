package com.taller2.chotuve.vista.componentes

import android.content.Context
import android.content.res.ColorStateList
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout
import com.taller2.chotuve.R
import java.util.*

class ClaveView constructor(context: Context, attrs: AttributeSet?) :  FrameLayout(context, attrs) {
    private var indicadorFuerzaClaveProgreso: ProgressBar
    private var indicadorFuerzaClaveTexto: TextView
    var clave: TextInputLayout
    var boton: Button? = null
    private val cambioEnTextoWatcher: TextWatcher

    constructor(context: Context) : this(context, null)

    init {
        View.inflate(context, R.layout.componente_clave, this)
        indicadorFuerzaClaveProgreso = findViewById<View>(R.id.indicador_fuerza_clave_progreso) as ProgressBar
        indicadorFuerzaClaveTexto = findViewById<View>(R.id.indicador_fuerza_clave_texto) as TextView
        clave = findViewById<View>(R.id.contraseña_input) as TextInputLayout

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
        clave.editText!!.addTextChangedListener(cambioEnTextoWatcher)
    }

    fun setHint(hint: String) {
        clave.hint = hint
    }

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

    private fun setearFuerzaClave(clave: String) {
        val fuerza = getFuerzaClave()

        indicadorFuerzaClaveTexto.text = TEXTOS_FUERZA[fuerza]
        indicadorFuerzaClaveProgreso.progressTintList =
            ColorStateList.valueOf(
                ContextCompat.getColor(context,
                COLOR_FUERZA[fuerza] ?: android.R.color.holo_red_dark
            ))
        indicadorFuerzaClaveProgreso.progress = fuerza
        boton?.isEnabled = fuerza >= 3
    }

    private fun getFuerzaClave(): Int {
        val clave = getClave()
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

    fun estaVacio(): Boolean {
        return getClave().isNullOrEmpty()
    }

    fun superaLargoMaximo(): Boolean {
        val largo = getClave().length
        return largo > 40
    }

    fun tieneFuerzaAceptable(): Boolean {
        return getFuerzaClave() >= 3
    }

    fun coincideCon(repetirClave: String): Boolean {
        return getClave() == repetirClave
    }

    fun getClave(): String {
        return clave.editText!!.text.toString()
    }

    fun setError(error: String) {
        clave.error = error
    }
}