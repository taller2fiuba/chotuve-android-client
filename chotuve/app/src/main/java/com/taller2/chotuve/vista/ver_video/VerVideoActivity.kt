package com.taller2.chotuve.vista.ver_video

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Video
import com.taller2.chotuve.modelo.interactor.InteractorVerVideo
import com.taller2.chotuve.presentador.PresentadorVerVideo
import com.taller2.chotuve.vista.principal.ID_KEY

class VerVideoActivity: AppCompatActivity(), VistaVerVideo {
    private val presentador = PresentadorVerVideo(this, InteractorVerVideo())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ver_video)

        val id = intent.getStringExtra(ID_KEY).toLong()
        presentador.obtenerVideo(id)

    }

    override fun onDestroy() {
        super.onDestroy()
        presentador.onDestroy()
    }

    override fun mostrarVideo(video: Video) {
        ocultarCargando()
        findViewById<TextView>(R.id.textViewUri).apply {
            text = video.uri.toString()
        }
    }

    fun ocultarCargando() {
        val cargando = findViewById<View>(R.id.cargando_video_barra_progreso) as ConstraintLayout
        cargando.visibility = View.GONE
    }

    override fun setErrorRed() {
        Log.d("vista", "Error del server")
        Toast.makeText(this, "Error del server", Toast.LENGTH_LONG).show()
    }
}