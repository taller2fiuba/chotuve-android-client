package com.taller2.chotuve.vista.componentes

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Usuario
import com.taller2.chotuve.util.cargarImagen

class UsuarioView constructor(context: Context, attrs: AttributeSet?) :  FrameLayout(context, attrs) {
    private var usuarioEmail: TextView
    private var usuarioFotoPerfil: ImageView
    private var usuarioContainer: LinearLayout

    constructor(context: Context) : this(context, null)

    init {
        View.inflate(context, R.layout.componente_usuario, this)
        usuarioEmail = findViewById<View>(R.id.usuario_email) as TextView
        usuarioFotoPerfil = findViewById<View>(R.id.usuario_foto_de_perfil) as ImageView
        usuarioContainer = findViewById<View>(R.id.usuario_container) as LinearLayout
    }

    fun setOnClickListener(clickListener : ((v: View) -> Unit)?) {
        usuarioContainer.setOnClickListener(clickListener)
    }

    fun setUsuario(usuario: Usuario) {
        usuarioEmail.text = usuario.email
        cargarImagen(usuario, usuarioFotoPerfil, this)
    }
}