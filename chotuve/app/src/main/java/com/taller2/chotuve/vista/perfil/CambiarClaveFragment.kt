package com.taller2.chotuve.vista.perfil

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.taller2.chotuve.R
import com.taller2.chotuve.presentador.PresentadorCambiarClave
import kotlinx.android.synthetic.main.fragment_cambiar_clave.*

class CambiarClaveFragment(private val email: String) : Fragment(), VistaCambiarClave {
    private val presentador = PresentadorCambiarClave(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cambiar_clave, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        boton_cambiar_contraseña.setOnClickListener {
            clickCambiar()
        }
    }

    fun clickCambiar() {
        clave_actual.error = ""
        nueva_clave.error = ""
        repetir_nueva_clave.error = ""

        val claveActual = clave_actual.editText?.text?.toString()
        val nuevaClave = nueva_clave.editText?.text?.toString()
        val repetirNuevaClave = repetir_nueva_clave.editText?.text?.toString()

        when {
            claveActual.isNullOrEmpty() -> clave_actual.error = "No puede estar vacío"
            nuevaClave.isNullOrEmpty() -> nueva_clave.error = "No puede estar vacío"
            nuevaClave != repetirNuevaClave -> repetir_nueva_clave.error = "Las contraseñas no coinciden"
            else -> {
                boton_cambiar_contraseña.isEnabled = false
                presentador.cambiarClave(email, claveActual, nuevaClave)
            }
        }
    }

    override fun setErrorRed() {
        Log.d("vista", "Error del server")
        boton_cambiar_contraseña.isEnabled = true
        Toast.makeText(context, "Error del server", Toast.LENGTH_LONG).show()
    }

    override fun setError() {
        boton_cambiar_contraseña.isEnabled = true
        clave_actual.error = "Contraseña incorrecta"
    }

    override fun onExito() {
        fragmentManager!!.popBackStack()
        Toast.makeText(context, "Bien! Contraseña actualizada", Toast.LENGTH_LONG).show()
    }

}
