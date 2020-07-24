package com.taller2.chotuve.vista.contactos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.SolicitudDeContacto
import com.taller2.chotuve.presentador.PresentadorSolicitudesDeContacto
import com.taller2.chotuve.vista.SeccionFragment
import com.taller2.chotuve.vista.componentes.SolicitudDeContactoView
import kotlinx.android.synthetic.main.fragment_solicitudes_de_contacto.*

class SolicitudesDeContactoFragment : Fragment(), VistaSolicitudesDeContacto {
    private val presentador = PresentadorSolicitudesDeContacto(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_solicitudes_de_contacto, container, false)  as View
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configurarRefreshLayout()
        presentador.obtenerSolicitudes()
    }

    override fun mostrarSolicitudes(solicitudes: List<SolicitudDeContacto>) {
        // TODO ordenando por id porque las solicitudes no tienen fecha de creacion
        val solicitudesOrdenados = solicitudes.sortedByDescending { it.id }
        solicitudesOrdenados.forEach { solicitud: SolicitudDeContacto ->
            val solicitudView = SolicitudDeContactoView(context!!, this, solicitud)
            solicitudes_container.addView(solicitudView)
        }
        cargando_solicitudes_barra_progreso.visibility = View.GONE
        swipeRefreshLayout.isRefreshing = false
        solicitudes_container.visibility = View.VISIBLE
        if (solicitudesOrdenados.isEmpty()) {
            sin_solicitudes.visibility = View.VISIBLE
        }
    }

    private fun configurarRefreshLayout() {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorSecondary)
        swipeRefreshLayout.setOnRefreshListener {
            sin_solicitudes.visibility = View.GONE
            solicitudes_container.removeAllViews()
            presentador.obtenerSolicitudes()
        }
    }

    override fun rechazarSolicitud(solicitudId: Long) {
        presentador.rechazarSolicitud(solicitudId)
    }

    override fun aceptarSolicitud(solicitudId: Long) {
        presentador.aceptarSolicitud(solicitudId)
    }

    override fun irAPerfilDeUsuario(usuarioId: Long) {
        val newFragment = SeccionFragment.perfil(usuarioId)
        val transicion = fragmentManager!!.beginTransaction()
        transicion.replace(R.id.fragment_container, newFragment)
        transicion.addToBackStack(null)
        transicion.commit()
    }

    override fun setErrorRed() {
        Log.d("vista", "Error del server")
        Toast.makeText(context!!, "Error del server", Toast.LENGTH_LONG).show()
    }
}