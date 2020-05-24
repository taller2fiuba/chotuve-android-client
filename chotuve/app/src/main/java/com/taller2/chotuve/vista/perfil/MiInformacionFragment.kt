package com.taller2.chotuve.vista.perfil

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taller2.chotuve.R

/**
 * A simple [Fragment] subclass.
 */
class MiInformacionFragment : Fragment() {
    companion object {
        fun newInstance(): MiInformacionFragment =
            MiInformacionFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mi_informacion, container, false)
    }

}
