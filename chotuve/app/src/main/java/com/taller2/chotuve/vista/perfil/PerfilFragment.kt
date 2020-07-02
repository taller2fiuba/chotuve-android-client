package com.taller2.chotuve.vista.perfil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.taller2.chotuve.R
import com.taller2.chotuve.vista.contactos.ContactosFragment

class PerfilFragment(val usuarioId: Long?) : Fragment() {
    companion object {
        fun newInstance(usuarioId: Long?): PerfilFragment =
            PerfilFragment(usuarioId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // TODO codigo repetido con principal fragment
        val view = inflater.inflate(R.layout.fragment_container, container, false)
        val fragment = VerPerfilFragment(usuarioId)
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
        return view
    }
}
