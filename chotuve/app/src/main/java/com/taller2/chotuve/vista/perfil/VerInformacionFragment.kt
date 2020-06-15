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
import com.taller2.chotuve.presentador.PresentadorPerfil
import kotlinx.android.synthetic.main.fragment_ver_informacion.*

class VerInformacionFragment(val usuarioId: Long?) : Fragment(), VistaInformacion {
    private val presentador = PresentadorPerfil(this)
    private var usuario: Usuario? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ver_informacion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mostrarCargandoPerfil()
        if (usuarioId == null) {
            // Mi perfil
            boton_agregar_a_contactos.visibility = View.GONE
        } else {
            editar_informacion.visibility = View.GONE
        }
        presentador.obtenerInformacion(usuarioId)
    }

    override fun onDestroy() {
        super.onDestroy()
        presentador.onDestroy()
    }

    fun mostrarCargandoPerfil() {
        cargando_barra_progreso.visibility = View.VISIBLE
    }

    override fun mostrarPerfil(usuario: Usuario) {
        // TODO tener en cuenta que pueden ser valores nelos si todavia no se completaron
        this.usuario = usuario
        nombre_y_apellido.text = getString(R.string.nombre_y_apellido, usuario.nombre, usuario.apellido)
        email.text = usuario.email
        telefono.text = usuario.telefono
        direccion.text = usuario.direccion
        Glide
            .with(this)
            .load(usuario.fotoPerfilUri)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .centerCrop()
            .into(imagen_perfil)
        editar_informacion.setOnClickListener {
            irAEditarInformacion()
        }
        cargando_barra_progreso.visibility = View.GONE
        perfil_informacion.visibility = View.VISIBLE
    }

    fun irAEditarInformacion() {
        val newFragment = EditarInformacionFragment(usuario!!)
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun setErrorRed() {
        Log.d("vista", "Error del server")
        Toast.makeText(context, "Error del server", Toast.LENGTH_LONG).show()
    }

}
