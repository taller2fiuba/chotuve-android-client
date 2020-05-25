package com.taller2.chotuve.vista.componentes

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.taller2.chotuve.R

class VideoPortadaConInformacion(context: Context, attrs: AttributeSet) :  ConstraintLayout(context, attrs) {
    private var portada: VideoPortada
    private var titulo: TextView
    private var autorYCreacion: TextView

    init {
        View.inflate(context, R.layout.video_portada_con_informacion, this)
        portada = findViewById<View>(R.id.video_portada) as VideoPortada
        titulo = findViewById<View>(R.id.titulo) as TextView
        autorYCreacion = findViewById<View>(R.id.autor_y_creacion) as TextView
    }

    fun setInformacionVideo(uri: Uri, duracion: Long, titulo: String, autor: String, creacion: String) {
        portada.setUri(uri, duracion)
        this.titulo.text = titulo
        autorYCreacion.text = context.getString(R.string.autor_y_creacion, autor, creacion)
        // TODO pasar a formato hace X años/meses/semanas/horas/minutos
        //val formateador = SimpleDateFormat("dd.MM.yyyy HH:mm")
        //this.creacion.text = formateador.format(creacion)
    }
}