package com.taller2.chotuve.vista.componentes

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.SolicitudDeContacto
import com.taller2.chotuve.vista.contactos.VistaSolicitudesDeContacto

class SolicitudDeContactoView(context: Context, vista: VistaSolicitudesDeContacto, solicitud: SolicitudDeContacto) :  ConstraintLayout(context) {
    private var autor_email: TextView
    private var rechazar: Button
    private var aceptar: Button

    init {
        View.inflate(context, R.layout.solicitud_de_contacto, this)
        autor_email = findViewById<View>(R.id.autor_solicitud_contacto) as TextView
        autor_email.text = solicitud.usuario.email
        rechazar = findViewById<View>(R.id.rechazar_solicitud_contacto) as Button
        aceptar = findViewById<View>(R.id.aceptar_solicitud_contacto) as Button

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
        autor_email.setOnClickListener {
            vista.irAPerfilDeUsuario(solicitud.usuario.usuarioId)
        }
    }
}