package com.taller2.chotuve.vista.perfil

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Usuario
import com.taller2.chotuve.presentador.PresentadorEditarPerfil
import kotlinx.android.synthetic.main.fragment_editar_informacion.*

class EditarInformacionFragment(val usuario: Usuario) : Fragment(), VistaEditarInformacion {
    private val presentador = PresentadorEditarPerfil(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_editar_informacion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        boton_editar.setOnClickListener {
            clickEditarPerfil()
        }
        nombre.editText?.setText(usuario.nombre)
        apellido.editText?.setText(usuario.apellido)
        telefono.editText?.setText(usuario.telefono)
        direccion.editText?.setText(usuario.direccion)
        Glide
            .with(this)
            .load(usuario.fotoPerfilUri)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .centerCrop()
            .into(imagen_perfil)
    }

    fun clickEditarPerfil() {
        val nombreString = nombre.editText?.text?.toString()
        val apellidoString = apellido.editText?.text?.toString()
        val telefonoString = telefono.editText?.text?.toString()
        val direccionString = direccion.editText?.text?.toString()
        when {
            nombreString.isNullOrEmpty() -> nombre.error = "No puede estar vacío"
            apellidoString.isNullOrEmpty() -> apellido.error = "No puede estar vacío"
            else -> {
                mostrarProgresoEdicion()
                presentador.editarPerfil(
                    nombreString,
                    apellidoString,
                    telefonoString,
                    direccionString
                )
            }
        }
    }

    fun mostrarProgresoEdicion() {
        editando_perfil_barra_progreso.visibility = View.VISIBLE
        deshabilitarBotonEdicion()
    }

    fun ocultarProgresoEdicion() {
        editando_perfil_barra_progreso.visibility = View.GONE
        habilitarBotonEdicion()
    }

    override fun habilitarBotonEdicion() {
        boton_editar.isEnabled = true
    }

    override fun deshabilitarBotonEdicion() {
        boton_editar.isEnabled = false
    }

    override fun setErrorRed() {
        Log.d("vista", "Error del server")
        boton_editar.isEnabled = true
        Toast.makeText(context, "Error del server", Toast.LENGTH_LONG).show()
    }

    override fun setError() {
        ocultarProgresoEdicion()
        Toast.makeText(context, "Error en el formulario", Toast.LENGTH_LONG).show()
    }

    override fun onEdicionExitosa() {
        activity!!.supportFragmentManager.popBackStack()
        Toast.makeText(context, "Ok editado", Toast.LENGTH_LONG).show()
    }

}
