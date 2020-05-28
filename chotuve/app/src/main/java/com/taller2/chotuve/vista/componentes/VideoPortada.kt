package com.taller2.chotuve.vista.componentes

import android.content.Context
import android.net.Uri
import android.text.format.DateUtils
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.taller2.chotuve.R

class VideoPortada(context: Context, attrs: AttributeSet) :  ConstraintLayout(context, attrs) {
    private var portada: ImageView
    private var duracion: TextView

    init {
        View.inflate(context, R.layout.video_portada, this)
        portada = findViewById<View>(R.id.portada) as ImageView
        duracion = findViewById<View>(R.id.duracion) as TextView
    }

    fun setUri(uri: Uri, duracion: Long) {
        Glide.with(this).load(uri).into(portada)

        val duracionString = DateUtils.formatElapsedTime(duracion)
        this.duracion.text = duracionString
    }
}