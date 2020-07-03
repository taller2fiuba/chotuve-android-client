package com.taller2.chotuve.vista.componentes

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Usuario

class UsuarioView constructor(context: Context, attrs: AttributeSet?) :  FrameLayout(context, attrs) {
    // TODO meter la foto de perfil acá
    private var usuarioEmail: TextView

    constructor(context: Context) : this(context, null)

    init {
        View.inflate(context, R.layout.componente_usuario, this)
        usuarioEmail = findViewById<View>(R.id.usuario_email) as TextView
    }

    fun setOnClickListener(clickListener : ((v: View) -> Unit)?) {
        usuarioEmail.setOnClickListener(clickListener)
    }

    fun setUsuario(usuario: Usuario) {
        usuarioEmail.text = usuario.email
    }
}