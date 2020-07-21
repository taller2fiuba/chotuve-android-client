package com.taller2.chotuve.vista.componentes

import android.content.Context
import android.view.View
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.SolicitudDeContacto
import com.taller2.chotuve.vista.contactos.VistaSolicitudesDeContacto

class SolicitudDeContactoView(context: Context, vista: VistaSolicitudesDeContacto, solicitud: SolicitudDeContacto) :  ConstraintLayout(context) {
    private var usuarioView: UsuarioView
    private var rechazar: ImageButton
    private var aceptar: ImageButton

    init {
        View.inflate(context, R.layout.componente_solicitud_de_contacto, this)
        usuarioView = findViewById<View>(R.id.usuario_solicitud_contacto) as UsuarioView
        usuarioView.setUsuario(solicitud.usuario)
        rechazar = findViewById<View>(R.id.rechazar_solicitud_contacto) as ImageButton
        aceptar = findViewById<View>(R.id.aceptar_solicitud_contacto) as ImageButton

        rechazar.setOnClickListener {
            vista.rechazarSolicitud(solicitud.id)
            rechazar.visibility = View.GONE
            aceptar.visibility = View.GONE
        }
        aceptar.setOnClickListener {
            vista.aceptarSolicitud(solicitud.id)
            rechazar.visibility = View.GONE
            aceptar.visibility = View.GONE
        }
        usuarioView.setOnClickListener {
            vista.irAPerfilDeUsuario(solicitud.usuario.id)
        }
    }
}