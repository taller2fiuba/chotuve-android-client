package com.taller2.chotuve.vista.perfil

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Modelo

class VideosFragment(val usuarioId: Long) : Fragment() {
    companion object {
        fun newInstance(usuarioId: Long): VideosFragment =
            VideosFragment(usuarioId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_videos, container, false)  as View
        val fab = view.findViewById(R.id.subir_video_boton) as FloatingActionButton
        if (usuarioId != Modelo.instance.id) {
            fab.visibility = View.GONE
        } else {
            fab.setOnClickListener {
                startActivityForResult(Intent(activity, SubirVideoActivity::class.java), 0)
            }
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            Toast.makeText(context, "Subida Exitosa :)", Toast.LENGTH_LONG).show()
        }
    }
}
