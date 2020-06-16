package com.taller2.chotuve.vista.ver_video

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Reaccion
import com.taller2.chotuve.modelo.Video
import com.taller2.chotuve.modelo.interactor.InteractorVerVideo
import com.taller2.chotuve.presentador.PresentadorVerVideo
import com.taller2.chotuve.util.obtenerAltoVideo
import com.taller2.chotuve.vista.principal.ID_KEY
import kotlinx.android.synthetic.main.controles_reproductor_video.*
import kotlinx.android.synthetic.main.ver_video.*

const val USUARIO_ID_KEY = "com.taller2.chotuve.USUARIO_ID_KEY"

class VerVideoActivity: AppCompatActivity(), VistaVerVideo {
    private val presentador = PresentadorVerVideo(this, InteractorVerVideo())

    private var player: SimpleExoPlayer? = null
    private var pantallaCompleta = false
    private var video: Video? = null
    private var playCuandoCargeVideo = true
    private var posicionInicial = 0
    private var playbackposition: Long = 0

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ver_video)

        setAltoReproductorVideo()
        icono_pantalla_completa.setOnClickListener {
            if (pantallaCompleta) {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                sacarReproductorDePantallaCompleta()
            } else {
                // esta no es la mejor forma de implementarlo, mejor seria con un fragmento, pero asi es mas facil
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                ponerReproductorEnPantallaCompleta()
            }
        }
        val id = intent.getStringExtra(ID_KEY)!!
        presentador.obtenerVideo(id)
    }

    override fun mostrarVideo(video: Video) {
        ocultarCargando()
        titulo.text = video.titulo
        creacion.text = video.creacion
        autor.text = video.autor.email
        if (!video.descripcion.isBlank()) {
            descripcion.text = video.descripcion
        } else {
            div2.visibility = View.GONE
            descripcion.visibility = View.GONE
        }
        this.video = video
        cantidad_me_gustas.text = video.reacciones!!.meGustas.toString()
        cantidad_no_me_gustas.text = video.reacciones.noMeGustas.toString()
        colorearReacciones(video.reacciones.miReaccion, null)
        inicializarReproductor()
    }

    fun ocultarCargando() {
        cargando_video_barra_progreso.visibility = View.GONE
        pantalla.visibility = View.VISIBLE
    }

    fun clickMeGusta(view: View) {
        presentador.reaccionar(video!!.id, Reaccion.ME_GUSTA)
        colorearReacciones(Reaccion.ME_GUSTA, video!!.reacciones!!.miReaccion)
    }

    fun clickNoMeGusta(view: View) {
        presentador.reaccionar(video!!.id, Reaccion.NO_ME_GUSTA)
        colorearReacciones(Reaccion.NO_ME_GUSTA, video!!.reacciones!!.miReaccion)
    }

    fun colorearReacciones(reaccion: Reaccion?, reaccionAnterior: Reaccion?) {
        var reaccionNueva = reaccion
        if (reaccion == reaccionAnterior) {
            // para despintar si volvi a tocar la misma reaccion que ya tenia
            reaccionNueva = null
        }
        when (reaccionNueva) {
            Reaccion.ME_GUSTA -> {
                colorear(me_gusta, R.color.colorSecondary)
                colorear(no_me_gusta, android.R.color.darker_gray)
            }
            Reaccion.NO_ME_GUSTA -> {
                colorear(me_gusta, android.R.color.darker_gray)
                colorear(no_me_gusta, R.color.colorSecondary)
            }
            else -> {
                colorear(me_gusta, android.R.color.darker_gray)
                colorear(no_me_gusta, android.R.color.darker_gray)
            }
        }
        video!!.reacciones!!.miReaccion = reaccionNueva
    }

    private fun colorear(boton: ImageButton, color: Int) {
        boton.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, color))
    }

    override fun setErrorRed() {
        Log.d("vista", "Error del server")
        Toast.makeText(this, "Error del server", Toast.LENGTH_LONG).show()
    }


    private fun buildMediaSource(uri: Uri): MediaSource {
        val datasourcefactory: DataSource.Factory = DefaultHttpDataSourceFactory("video")
        return ProgressiveMediaSource.Factory(datasourcefactory)
            .createMediaSource(uri)
    }

    private fun inicializarReproductor() {
        if (player == null && video != null) {
            player = ExoPlayerFactory.newSimpleInstance(this)
            video_reproductor.player = player
            val mediaSource = buildMediaSource(video!!.uri)
            player!!.playWhenReady = playCuandoCargeVideo
            player!!.seekTo(posicionInicial, playbackposition)
            player!!.prepare(mediaSource, false, false)
        }
    }

    override fun onStart() {
        super.onStart()
        inicializarReproductor()
    }

    override fun onResume() {
        super.onResume()
        inicializarReproductor()
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun releasePlayer() {
        if (player != null) {
            playCuandoCargeVideo = player!!.playWhenReady
            playbackposition = player!!.currentPosition
            posicionInicial = player!!.currentWindowIndex
            player!!.release()
            player = null
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        player!!.stop()
        releasePlayer()
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        val currentOrientation = resources.configuration.orientation
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            ponerReproductorEnPantallaCompleta()
        } else {
            sacarReproductorDePantallaCompleta()
        }
    }

    private fun ponerReproductorEnPantallaCompleta() {
        icono_pantalla_completa.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.exo_controls_fullscreen_exit
            )
        )
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        val params = video_reproductor.layoutParams as ConstraintLayout.LayoutParams
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        video_reproductor.layoutParams = params
        pantallaCompleta = true
    }

    private fun sacarReproductorDePantallaCompleta() {
        icono_pantalla_completa.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.exo_controls_fullscreen_enter
            )
        )
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        setAltoReproductorVideo()
        pantallaCompleta = false
    }

    private fun setAltoReproductorVideo() {
        val params = video_reproductor.layoutParams as ConstraintLayout.LayoutParams
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = obtenerAltoVideo(applicationContext)
        video_reproductor.layoutParams = params
    }

    fun clickAutor(view: View) {
        var data = Intent()
        data.putExtra(USUARIO_ID_KEY, video!!.autor.usuarioId.toString())
        setResult(RESULT_OK, data)
        finish()
    }
}