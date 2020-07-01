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
import com.taller2.chotuve.modelo.EstadoContacto
import com.taller2.chotuve.modelo.PerfilDeUsuario
import com.taller2.chotuve.presentador.PresentadorPerfil
import kotlinx.android.synthetic.main.fragment_ver_informacion.*

class InformacionFragment(val usuarioId: Long?) : Fragment(), VistaInformacion {
    companion object {
        fun newInstance(usuarioId: Long?): InformacionFragment =
            InformacionFragment(usuarioId)
    }

    private val AUN_NO_COMPLETADO = "Aún no completado"

    private val presentador = PresentadorPerfil(this)
    private var perfilDeUsuario: PerfilDeUsuario? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ver_informacion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mostrarCargandoPerfil()
        if (usuarioId != null) {
            // perfil de otro usuario
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

    override fun mostrarPerfil(perfilDeUsuario: PerfilDeUsuario) {
        this.perfilDeUsuario = perfilDeUsuario
        if (perfilDeUsuario.nombre != null && perfilDeUsuario.apellido != null) {
            nombre_y_apellido.text = getString(R.string.nombre_y_apellido, perfilDeUsuario.nombre, perfilDeUsuario.apellido)
        } else {
            nombre_y_apellido.text = AUN_NO_COMPLETADO
        }
        email.text = perfilDeUsuario.usuario.email
        telefono.text = perfilDeUsuario.telefono ?: AUN_NO_COMPLETADO
        direccion.text = perfilDeUsuario.direccion ?: AUN_NO_COMPLETADO
        if (perfilDeUsuario.fotoPerfilUri != null) {
            Glide
                .with(this)
                .load(perfilDeUsuario.fotoPerfilUri)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .centerCrop()
                .into(imagen_perfil)
        }
        editar_informacion.setOnClickListener {
            irAEditarInformacion()
        }
        if (usuarioId != null) {
            when (perfilDeUsuario.estadoContacto) {
                EstadoContacto.ES_CONTACTO -> boton_en_contactos.visibility = View.VISIBLE
                EstadoContacto.SOLICITUD_ENVIADA -> boton_solitud_enviada.visibility = View.VISIBLE
                EstadoContacto.SOLICITUD_PENDIENTE -> {
                    aceptar_rechazar_container.visibility = View.VISIBLE
                    // TODO falta el id de solicitud para poder aceptar y rechazar
                }
                null -> {
                    boton_agregar_a_contactos.visibility = View.VISIBLE
                    boton_agregar_a_contactos.setOnClickListener {
                        enviarSolicitudDeContacto(perfilDeUsuario.usuario.id)
                        boton_agregar_a_contactos.visibility = View.GONE
                        boton_solitud_enviada.visibility = View.VISIBLE
                    }
                }
            }
        }
        cargando_barra_progreso.visibility = View.GONE
        perfil_informacion.visibility = View.VISIBLE
    }

    fun enviarSolicitudDeContacto(usuarioId: Long) {
        presentador.enviarSolicitudDeContacto(usuarioId)
    }

    fun irAEditarInformacion() {
        val newFragment = EditarInformacionFragment(perfilDeUsuario!!)
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container_navegacion, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun setErrorRed() {
        Log.d("vista", "Error del server")
        Toast.makeText(context, "Error del server", Toast.LENGTH_LONG).show()
    }

}
