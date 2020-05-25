package com.taller2.chotuve.vista.principal

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.taller2.chotuve.R
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
        val view = inflater.inflate(R.layout.fragment_principal, container, false)
        presentador.obtenerVideo()
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        presentador.onDestroy()
    }

    override fun mostrarVideo(uri: Uri, duracion: Long, titulo: String, autor: String, creacion: String) {
        val videoPortada = view!!.findViewById<View>(R.id.video) as VideoPortadaConInformacion

        videoPortada.setInformacionVideo(uri, duracion, titulo, autor, creacion)
    }

    override fun setErrorRed() {
        Log.d("vista", "Error del server")
        Toast.makeText(context, "Error del server", Toast.LENGTH_LONG).show()
    }

    override fun mostrarCargandoVideo() {
        Toast.makeText(context, "cargando", Toast.LENGTH_LONG).show()
    }

}
