package com.taller2.chotuve.vista.principal

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taller2.chotuve.R
import com.taller2.chotuve.vista.componentes.VideoPortadaConInformacion
import java.time.LocalDate
import java.time.LocalDateTime

class PrincipalFragment : Fragment() {
    companion object {
        fun newInstance(): PrincipalFragment =
            PrincipalFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_principal, container, false)
        val video = view.findViewById<View>(R.id.video) as VideoPortadaConInformacion
        val uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/chotuve-a8587.appspot.com/o/VID-20200426-WA0000.mp4?alt=media&token=f1ef2901-3f9f-4e1c-9b6f-519c4297b6c1")
        video.setInformacionVideo(uri, "el titulo", "yo papa", "28/02/2018")
        return view

    }

}
