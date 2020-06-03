package com.taller2.chotuve.vista.ver_video

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
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
import com.google.android.exoplayer2.util.Util
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Video
import com.taller2.chotuve.modelo.interactor.InteractorVerVideo
import com.taller2.chotuve.presentador.PresentadorVerVideo
import com.taller2.chotuve.util.obtenerAltoVideo
import com.taller2.chotuve.vista.principal.ID_KEY
import kotlinx.android.synthetic.main.controles_reproductor_video.*
import kotlinx.android.synthetic.main.ver_video.*


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

    override fun onDestroy() {
        super.onDestroy()
        presentador.onDestroy()
    }

    override fun mostrarVideo(video: Video) {
        ocultarCargando()
        titulo.text = video.titulo
        autor_y_creacion.text = getString(R.string.autor_y_creacion, video.autor, video.creacion)
        this.video = video
        inicializarReproductor()
    }

    fun ocultarCargando() {
        cargando_video_barra_progreso.visibility = View.GONE
        pantalla.visibility = View.VISIBLE
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
}