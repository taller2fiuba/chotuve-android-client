package com.taller2.chotuve.vista

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.taller2.chotuve.R
import com.taller2.chotuve.vista.perfil.PerfilViewPagerFragment
import com.taller2.chotuve.vista.principal.MuroDeVideosFragment

open class SeccionFragment(val primerFragment: Fragment) : Fragment() {
    companion object {
        fun perfil(usuarioId: Long?): SeccionFragment =
            SeccionFragment(PerfilViewPagerFragment(usuarioId))

        fun principal(): SeccionFragment =
            SeccionFragment(MuroDeVideosFragment())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_container, container, false)
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, primerFragment)
        transaction.commit()
        return view
    }
}
