package com.taller2.chotuve.vista.perfil

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taller2.chotuve.R

class InformacionFragment(val usuarioId: Long?) : Fragment() {
    companion object {
        fun newInstance(usuarioId: Long?): InformacionFragment =
            InformacionFragment(usuarioId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_informacion, container, false)
        val newFragment = VerInformacionFragment(usuarioId)
        val transaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.fragment_container, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
        return view
    }

}
