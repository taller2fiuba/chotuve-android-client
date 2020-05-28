package com.taller2.chotuve.vista.componentes

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Video

class VideoPortadaConInformacion(context: Context, attributeSet: AttributeSet) :  ConstraintLayout(context, attributeSet) {
    private var portada: VideoPortada
    private var titulo: TextView
    private var autorYCreacion: TextView

    init {
        View.inflate(context, R.layout.video_portada_con_informacion, this)
        portada = findViewById<View>(R.id.video_portada) as VideoPortada
        titulo = findViewById<View>(R.id.titulo) as TextView
        autorYCreacion = findViewById<View>(R.id.autor_y_creacion) as TextView
    }

    fun setVideo(video: Video) {
        portada.setUri(video.uri, video.duracion)
        titulo.text = video.titulo
        autorYCreacion.text = context.getString(R.string.autor_y_creacion, video.autor, video.creacion)
        // TODO pasar a formato hace X años/meses/semanas/horas/minutos
        //val formateador = SimpleDateFormat("dd.MM.yyyy HH:mm")
        //this.creacion.text = formateador.format(creacion)
    }
}