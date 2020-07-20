package com.taller2.chotuve.vista.componentes

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.format.DateUtils
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.taller2.chotuve.R
import com.taller2.chotuve.util.obtenerAltoVideo

class VideoPortada(context: Context, attrs: AttributeSet) :  ConstraintLayout(context, attrs) {
    private var portada: ImageView
    private var duracion: TextView
    private var cargando: ProgressBar

    init {
        View.inflate(context, R.layout.video_portada, this)
        portada = findViewById<View>(R.id.portada) as ImageView
        val params = portada.layoutParams as LayoutParams
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = obtenerAltoVideo(context.applicationContext)
        portada.layoutParams = params
        duracion = findViewById<View>(R.id.duracion) as TextView
        cargando = findViewById<View>(R.id.cargando_barra_progreso) as ProgressBar
    }

    fun setUri(uri: Uri, duracion: Long) {
        Glide
            .with(context)
            .load(uri)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .centerCrop()
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    cargando.visibility = View.GONE
                    return false
                }
            })
            .into(portada)

        val duracionString = DateUtils.formatElapsedTime(duracion)
        this.duracion.text = duracionString
    }
}