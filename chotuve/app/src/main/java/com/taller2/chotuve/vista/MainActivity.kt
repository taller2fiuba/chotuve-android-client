package com.taller2.chotuve.vista

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Modelo
import com.taller2.chotuve.vista.autenticacion.RegistrarmeActivity
import com.taller2.chotuve.vista.chats.ChatsFragment
import com.taller2.chotuve.vista.notificaciones.NotificacionesFragment
import com.taller2.chotuve.vista.perfil.PerfilFragment
import com.taller2.chotuve.vista.principal.PrincipalFragment


class MainActivity : AppCompatActivity() {

    val FRAGMENT_PRINCIPAL_TAG =  "FRAGMENT_PRINCIPAL_TAG"
    val FRAGMENT_CHATS_TAG =  "FRAGMENT_CHATS_TAG"
    val FRAGMENT_NOTIFICACIONES_TAG =  "FRAGMENT_NOTIFICACIONES_TAG"
    val FRAGMENT_PERFIL_TAG =  "FRAGMENT_PERFIL_TAG"

    private lateinit var navegacion : BottomNavigationView
    private val principalFragment: Fragment = PrincipalFragment.newInstance()
    private val chatsFragment: Fragment = ChatsFragment.newInstance()
    private val notificacionesFragment: Fragment = NotificacionesFragment.newInstance()
    private val perfilFragment: Fragment = PerfilFragment.newInstance()
    private var fragmentActivo : Fragment = principalFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!Modelo.instance.estaLogueado())
            startActivity(Intent(this, RegistrarmeActivity::class.java))
        else {
            setContentView(R.layout.home)

            supportFragmentManager.beginTransaction().add(R.id.container_navegacion, principalFragment, FRAGMENT_PRINCIPAL_TAG).commit()

            navegacion = findViewById(R.id.navegacion)
            navegacion.setOnNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.principal_menu -> {
                        openFragment(FRAGMENT_PRINCIPAL_TAG, principalFragment)
                        true
                    }
                    R.id.chats_menu -> {
                        openFragment(FRAGMENT_CHATS_TAG, chatsFragment)
                        true
                    }
                    R.id.notificaciones_menu -> {
                        openFragment(FRAGMENT_NOTIFICACIONES_TAG, notificacionesFragment)
                        true
                    }
                    R.id.perfil_menu -> {
                        openFragment(FRAGMENT_PERFIL_TAG, perfilFragment)
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun openFragment(tag: String, fragment: Fragment) {
        var fragmentoSeleccionado = supportFragmentManager.findFragmentByTag(tag)
        val transicion = supportFragmentManager.beginTransaction().hide(fragmentActivo)
        if (fragmentoSeleccionado == null) {
            transicion.add(R.id.container_navegacion, fragment, tag).commit()
        } else {
            transicion.show(fragment).commit()
        }
        fragmentActivo = fragment
    }


}
