package com.taller2.chotuve

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MisVideosFragment : Fragment() {
    companion object {
        fun newInstance(): MisVideosFragment = MisVideosFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_mis_videos, container, false)  as View
        val fab = view.findViewById(R.id.subir_video_boton) as FloatingActionButton
        fab.setOnClickListener { view ->
            startActivity(Intent(activity, SubirVideoActivity::class.java))
        }
        return view
    }
}
