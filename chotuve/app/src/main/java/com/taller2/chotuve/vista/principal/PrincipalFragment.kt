package com.taller2.chotuve.vista.principal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Video
import com.taller2.chotuve.modelo.interactor.InteractorPrincipal
import com.taller2.chotuve.presentador.PresentadorPrincipal
import com.taller2.chotuve.vista.componentes.VideoPortadaConInformacion

class PrincipalFragment : Fragment(), VistaPrincipal {
    private val presentador = PresentadorPrincipal(this, InteractorPrincipal())
    companion object {
        fun newInstance(): PrincipalFragment =
            PrincipalFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_principal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presentador.obtenerVideos()
    }

    override fun onDestroy() {
        super.onDestroy()
        presentador.onDestroy()
    }

    override fun mostrarVideos(videos: List<Video>) {
        ocultarCargandoVideo()
        val linearLayout = view!!.findViewById<View>(R.id.linear_layout) as LinearLayout
        linearLayout.visibility = View.VISIBLE
        videos.forEach { video: Video ->
            val videoPortada = VideoPortadaConInformacion(context!!, video)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 0, 48)
            videoPortada.layoutParams = params
            linearLayout.addView(videoPortada)
        }
    }

    override fun setErrorRed() {
        Log.d("vista", "Error del server")
        Toast.makeText(context, "Error del server", Toast.LENGTH_LONG).show()
    }

    override fun mostrarCargandoVideo() {
        val cargando = view!!.findViewById<View>(R.id.cargando_video_barra_progreso) as ConstraintLayout
        cargando.visibility = View.VISIBLE
    }

    fun ocultarCargandoVideo() {
        val cargando = view!!.findViewById<View>(R.id.cargando_video_barra_progreso) as ConstraintLayout
        cargando.visibility = View.GONE
    }

}
