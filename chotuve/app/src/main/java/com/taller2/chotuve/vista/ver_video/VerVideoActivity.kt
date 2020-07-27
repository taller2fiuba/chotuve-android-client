package com.taller2.chotuve.vista.ver_video

import android.animation.Animator
import android.annotation.SuppressLint
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.*
import com.taller2.chotuve.modelo.interactor.InteractorVerVideo
import com.taller2.chotuve.presentador.PresentadorVerVideo
import com.taller2.chotuve.util.formatearFechaSegunDia
import com.taller2.chotuve.util.obtenerAltoVideo
import com.taller2.chotuve.vista.principal.ID_KEY
import com.taller2.chotuve.vista.scroll_infinito.ComentariosAdapter
import com.taller2.chotuve.vista.scroll_infinito.ScrollInfinitoListener
import kotlinx.android.synthetic.main.controles_reproductor_video.*
import kotlinx.android.synthetic.main.ver_video.*
import org.joda.time.LocalDateTime

const val USUARIO_ID_KEY = "com.taller2.chotuve.USUARIO_ID_KEY"

class VerVideoActivity: AppCompatActivity(), VistaVerVideo {
    private val presentador = PresentadorVerVideo(this, InteractorVerVideo())
    val modelo = Modelo.instance

    private var player: SimpleExoPlayer? = null
    private var pantallaCompleta = false
    private var video: Video? = null
    private var playCuandoCargeVideo = true
    private var posicionInicial = 0
    private var playbackposition: Long = 0

    private lateinit var scrollInfinitoListener: ScrollInfinitoListener
    private lateinit var adapter: ComentariosAdapter

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
        configurarRecyclerView()
        presentador.obtenerVideo(id)
    }

    override fun onDestroy() {
        super.onDestroy()
        presentador.onDestroy()
    }

    fun configurarRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this)
        comentarios_recycler_view.layoutManager = linearLayoutManager
        scrollInfinitoListener = object : ScrollInfinitoListener(linearLayoutManager) {
            override fun onLoadMore(pagina: Int, cantidadDeItems: Int, view: RecyclerView?) {
                comentarios_recycler_view.post {
                    // esto hace que el cargando se ejecute justo despues de que esta funcion termine
                    // ahora no se puede agregar porque no podes agregar items al mismo tiempo que scrolleas
                    adapter.agregarCargando()
                }
                presentador.obtenerComentarios(video!!.id, pagina)
            }
        }
        adapter = ComentariosAdapter(this)
        comentarios_recycler_view.adapter = adapter
        comentarios_recycler_view.addOnScrollListener(scrollInfinitoListener)
    }

    override fun mostrarVideo(video: Video) {
        ocultarCargando()
        titulo.text = video.titulo
        creacion.text = video.creacion
        autor.setUsuario(video.autor)
        autor.setOnClickListener {
            clickAutor()
        }
        if (!video.descripcion.isBlank()) {
            descripcion.text = video.descripcion
        } else {
            div2.visibility = View.GONE
            descripcion.visibility = View.GONE
        }
        this.video = video
        setearReacciones(video.reacciones!!.miReaccion, null)
        setearCantidadDeComentarios(video.cantidadComentarios)
        inicializarReproductor()
        presentador.obtenerComentarios(video.id, 0)
        // arrancar fuera de la pantalla
        comentarios_container.animate()
            .translationY(informacion_video_container.height.toFloat())
    }

    override fun mostrarComentarios(comentarios: List<Comentario>) {
        cargando_comentarios_barra_progreso.visibility = View.GONE
        adapter.sacarCargando()
        comentarios_recycler_view.visibility = View.VISIBLE
        adapter.addAll(comentarios)
    }

    fun ocultarCargando() {
        cargando_video_barra_progreso.visibility = View.GONE
        pantalla.visibility = View.VISIBLE
    }

    fun clickMeGusta(view: View) {
        presentador.reaccionar(video!!.id, Reaccion.ME_GUSTA)
        setearReacciones(Reaccion.ME_GUSTA, video!!.reacciones!!.miReaccion, true)
    }

    fun clickNoMeGusta(view: View) {
        presentador.reaccionar(video!!.id, Reaccion.NO_ME_GUSTA)
        setearReacciones(Reaccion.NO_ME_GUSTA, video!!.reacciones!!.miReaccion, true)
    }

    fun setearReacciones(reaccion: Reaccion?, reaccionAnterior: Reaccion?, esClick: Boolean = false) {
        var reaccionNueva = reaccion
        if (reaccion == reaccionAnterior) {
            // para despintar si volvi a tocar la misma reaccion que ya tenia
            reaccionNueva = null
        }
        var nuevaCantidadMeGusta = video!!.reacciones!!.meGustas
        var nuevaCantidadNoMeGusta = video!!.reacciones!!.noMeGustas
        when (reaccionNueva) {
            Reaccion.ME_GUSTA -> {
                colorear(me_gusta, R.color.colorSecondary)
                colorear(no_me_gusta, android.R.color.darker_gray)
                if (esClick) {
                    nuevaCantidadMeGusta += 1
                    if (reaccionAnterior == Reaccion.NO_ME_GUSTA) {
                        nuevaCantidadNoMeGusta -= 1
                    }
                }
            }
            Reaccion.NO_ME_GUSTA -> {
                colorear(me_gusta, android.R.color.darker_gray)
                colorear(no_me_gusta, R.color.colorSecondary)
                if (esClick) {
                    nuevaCantidadNoMeGusta += 1
                    if (reaccionAnterior == Reaccion.ME_GUSTA) {
                        nuevaCantidadMeGusta -= 1
                    }
                }
            }
            else -> {
                colorear(me_gusta, android.R.color.darker_gray)
                colorear(no_me_gusta, android.R.color.darker_gray)
                if (esClick) {
                    if (reaccionAnterior == Reaccion.ME_GUSTA) {
                        nuevaCantidadMeGusta -= 1
                    } else if (reaccionAnterior == Reaccion.NO_ME_GUSTA) {
                        nuevaCantidadNoMeGusta -= 1
                    }
                }
            }
        }
        cantidad_me_gustas.text = nuevaCantidadMeGusta.toString()
        cantidad_no_me_gustas.text = nuevaCantidadNoMeGusta.toString()
        video!!.reacciones!!.meGustas = nuevaCantidadMeGusta
        video!!.reacciones!!.noMeGustas = nuevaCantidadNoMeGusta
        video!!.reacciones!!.miReaccion = reaccionNueva
    }

    fun setearCantidadDeComentarios(cantidad: Long) {
        cantidad_comentarios_extendido.text = cantidad.toString()
        cantidad_comentarios.text = cantidad.toString()
    }

    private fun colorear(boton: ImageButton, color: Int) {
        boton.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, color))
    }

    fun clickAutor() {
        irAPerfilDeUsuario(video!!.autor.id)
    }

    fun irAPerfilDeUsuario(usuarioId: Long) {
        var data = Intent()
        data.putExtra(USUARIO_ID_KEY, usuarioId.toString())
        setResult(RESULT_OK, data)
        finish()
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
        if (comentarios_container.visibility == View.VISIBLE) {
            clickCerrarComentarios()
        } else {
            super.onBackPressed()
            player!!.stop()
            releasePlayer()
            finish()
        }
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

    fun clickMostrarComentarios(view: View) {
        comentarios_container.animate()
            .translationY(0F)
            .alpha(1.0f)
            .setListener(object: Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}

                override fun onAnimationEnd(animation: Animator) {
                    informacion_video_container.visibility = View.GONE
                }

                override fun onAnimationCancel(animation: Animator?) {}

                override fun onAnimationStart(animation: Animator?) {
                    comentarios_container.visibility = View.VISIBLE
                    comentarios_container.alpha = 0.0f
                }
            })
    }

    fun clickCerrarComentarios(view: View? = null) {
        comentarios_container.animate()
            .translationY(comentarios_container.height.toFloat())
            .alpha(0.0f)
            .setListener(object: Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}

                override fun onAnimationEnd(animation: Animator) {
                    comentarios_container.visibility = View.GONE
                }

                override fun onAnimationCancel(animation: Animator?) {}

                override fun onAnimationStart(animation: Animator?) {
                    informacion_video_container.visibility = View.VISIBLE
                }
            })
    }

    fun clickAgregarComentario(view: View) {
        Log.d("vista", "ejecutando")
        val comentarioString = comentario.editText?.text?.toString()
        when {
            comentarioString.isNullOrEmpty() -> comentario.error = "No puede estar vacío"
            else -> {
                crear_comentario_boton.visibility = View.GONE
                creando_comentario_barra_progreso.visibility = View.VISIBLE
                presentador.crearComentario(video!!.id, comentarioString)
            }
        }
    }

    override fun agregarNuevoComentario(nuevoComentario: String, miPerfil: PerfilDeUsuario) {
        comentario.editText?.setText("")
        video!!.cantidadComentarios++
        setearCantidadDeComentarios(video!!.cantidadComentarios)
        crear_comentario_boton.visibility = View.VISIBLE
        creando_comentario_barra_progreso.visibility = View.GONE
        adapter.add(0, Comentario(miPerfil.usuario, formatearFechaSegunDia(LocalDateTime.now()), nuevoComentario))
        comentarios_recycler_view.smoothScrollToPosition(0)
    }
}