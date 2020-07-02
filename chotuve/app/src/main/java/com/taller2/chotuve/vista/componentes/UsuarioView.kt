package com.taller2.chotuve.vista.componentes

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Usuario

class UsuarioView(context: Context, usuario: Usuario) :  FrameLayout(context) {
    // TODO meter la foto de perfil acá
    private var usuarioEmail: TextView

    init {
        View.inflate(context, R.layout.componente_usuario, this)
        usuarioEmail = findViewById<View>(R.id.usuario_email) as TextView
        usuarioEmail.text = usuario.email
    }

    fun setOnClickListener(clickListener : ((v: View) -> Unit)?) {
        usuarioEmail.setOnClickListener(clickListener)
    }
}