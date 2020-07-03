package com.taller2.chotuve.vista.componentes

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Mensaje


class MensajeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var contenido: TextView = itemView.findViewById<View>(R.id.contenido_mensaje) as TextView
    private var fechaYHora: TextView = itemView.findViewById<View>(R.id.hora_mensaje) as TextView

    fun setMensaje(mensaje: Mensaje) {
        contenido.text = mensaje.mensaje
        fechaYHora.text =  mensaje.fechaYHora
    }
}