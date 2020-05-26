package com.taller2.chotuve.vista.componentes

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Video

class VideoPortadaConInformacion(context: Context, video: Video) :  ConstraintLayout(context) {
    private var portada: VideoPortada
    private var titulo: TextView
    private var autorYCreacion: TextView

    init {
        View.inflate(context, R.layout.video_portada_con_informacion, this)
        portada = findViewById<View>(R.id.video_portada) as VideoPortada
        portada.setUri(video.uri, video.duracion)

        titulo = findViewById<View>(R.id.titulo) as TextView
        this.titulo.text = video.titulo

        autorYCreacion = findViewById<View>(R.id.autor_y_creacion) as TextView
        autorYCreacion.text = context.getString(R.string.autor_y_creacion, video.autor, video.creacion)
        // TODO pasar a formato hace X años/meses/semanas/horas/minutos
        //val formateador = SimpleDateFormat("dd.MM.yyyy HH:mm")
        //this.creacion.text = formateador.format(creacion)
    }
}