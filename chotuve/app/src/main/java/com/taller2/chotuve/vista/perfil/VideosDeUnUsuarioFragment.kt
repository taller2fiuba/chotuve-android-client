package com.taller2.chotuve.vista.perfil

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Modelo
import com.taller2.chotuve.presentador.PresentadorVideosDeUnUsuario
import com.taller2.chotuve.vista.principal.VideosFragment

const val SUBIR_VIDEO_REQUEST_CODE = 1

class VideosDeUnUsuarioFragment(val usuarioId: Long, fm: FragmentManager) : VideosFragment(fm) {
    override val presentador = PresentadorVideosDeUnUsuario(usuarioId, this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fab = view.findViewById(R.id.subir_video_boton) as FloatingActionButton
        if (usuarioId == Modelo.instance.id) {
            fab.visibility = View.VISIBLE
            fab.setOnClickListener {
                startActivityForResult(Intent(activity, SubirVideoActivity::class.java), SUBIR_VIDEO_REQUEST_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == SUBIR_VIDEO_REQUEST_CODE) {
            Toast.makeText(context, "Subida Exitosa :)", Toast.LENGTH_LONG).show()
        }
    }
}
