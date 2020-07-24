package com.taller2.chotuve.vista

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.taller2.chotuve.R
import com.taller2.chotuve.vista.chats.ChatsFragment
import com.taller2.chotuve.vista.notificaciones.NotificacionesFragment
import com.taller2.chotuve.vista.perfil.PerfilViewPagerFragment
import com.taller2.chotuve.vista.principal.MuroDeVideosFragment

class SeccionFragment(val primerFragment: Fragment) : Fragment() {
    companion object {
        fun perfil(usuarioId: Long): SeccionFragment =
            SeccionFragment(PerfilViewPagerFragment(usuarioId))

        fun principal(): SeccionFragment =
            SeccionFragment(MuroDeVideosFragment())

        fun chats(): SeccionFragment =
            SeccionFragment(ChatsFragment())

        fun notificaciones(): SeccionFragment =
            SeccionFragment(NotificacionesFragment())
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

    fun reseleccionar() {
        if (childFragmentManager.backStackEntryCount == 0) {
            if (primerFragment::class == MuroDeVideosFragment::class) {
                (primerFragment as MuroDeVideosFragment).scrollTop()
            }
        } else {
            childFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }
}
