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
        presentador.obtenerSolicitudes()
    }

    override fun mostrarSolicitudes(solicitudes: List<SolicitudDeContacto>) {
        solicitudes.forEach { solicitud: SolicitudDeContacto ->
            val solicitudView = SolicitudDeContactoView(context!!, this, solicitud)
            solicitudes_container.addView(solicitudView)
        }
        cargando_solicitudes_barra_progreso.visibility = View.GONE
        solicitudes_container.visibility = View.VISIBLE
    }

    override fun rechazarSolicitud(solicitudId: Long) {
        presentador.rechazarSolicitud(solicitudId)
    }

    override fun aceptarSolicitud(solicitudId: Long) {
        presentador.aceptarSolicitud(solicitudId)
    }

    override fun setErrorRed() {
        Log.d("vista", "Error del server")
        Toast.makeText(context!!, "Error del server", Toast.LENGTH_LONG).show()
    }
}