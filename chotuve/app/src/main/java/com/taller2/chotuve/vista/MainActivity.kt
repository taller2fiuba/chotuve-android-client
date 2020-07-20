package com.taller2.chotuve.vista

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Modelo
import com.taller2.chotuve.vista.autenticacion.RegistrarmeActivity


class MainActivity : AppCompatActivity() {

    private val FRAGMENT_PRINCIPAL_TAG =  "FRAGMENT_PRINCIPAL_TAG"
    private val FRAGMENT_PRINCIPAL_INDEX =  0
    private val FRAGMENT_CHATS_TAG =  "FRAGMENT_CHATS_TAG"
    private val FRAGMENT_CHATS_INDEX =  1
    private val FRAGMENT_NOTIFICACIONES_TAG =  "FRAGMENT_NOTIFICACIONES_TAG"
    private val FRAGMENT_NOTIFICACIONES_INDEX =  2
    private val FRAGMENT_PERFIL_TAG =  "FRAGMENT_PERFIL_TAG"
    private val FRAGMENT_PERFIL_INDEX =  3

    private lateinit var navegacion : BottomNavigationView
    private val principalFragment: SeccionFragment = SeccionFragment.principal()
    private val chatsFragment: SeccionFragment = SeccionFragment.chats()
    private val notificacionesFragment: SeccionFragment = SeccionFragment.notificaciones()
    private lateinit var perfilFragment: SeccionFragment
    private var fragmentActivo : SeccionFragment = principalFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!Modelo.instance.estaLogueado())
            startActivity(Intent(this, RegistrarmeActivity::class.java))
        else {
            setContentView(R.layout.home)

            perfilFragment = SeccionFragment.perfil(Modelo.instance.id!!)
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
            navegacion.setOnNavigationItemReselectedListener {
                fragmentActivo.reseleccionar()
            }
        }
    }

    private fun openFragment(tag: String, fragment: SeccionFragment) {
        val fragmentoExistente = supportFragmentManager.findFragmentByTag(tag)
        val transicion = supportFragmentManager.beginTransaction().hide(fragmentActivo)
        // Si es fragmento todavia no se creo crearlo
        if (fragmentoExistente == null) {
            transicion.add(R.id.container_navegacion, fragment, tag)
        } else {
            transicion.show(fragment)
        }
        transicion.addToBackStack(tag)
        transicion.commit()
        // +1 por el que se acaba de agregar, aun no afecta
        val cantidadStack: Int = supportFragmentManager.backStackEntryCount + 1
        supportFragmentManager.addOnBackStackChangedListener(object : FragmentManager.OnBackStackChangedListener {
            override fun onBackStackChanged() {
                // Si cantidad en el stack baja entonces tocaron el boton de back
                if (supportFragmentManager.backStackEntryCount in 1 until cantidadStack) {
                    supportFragmentManager.removeOnBackStackChangedListener(this)
                    val tagAnterior =
                        supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 1).name
                    // seteo el icono a mostrar en la nevegacion con el index del fragment anterior
                    navegacion.menu.getItem(getIndex(tagAnterior)).isChecked = true
                    fragmentActivo = getFragment(tagAnterior)
                } else if (supportFragmentManager.backStackEntryCount == 0) {
                    // volvi al principio de todo, es la pantalla principal
                    navegacion.menu.getItem(FRAGMENT_PRINCIPAL_INDEX).isChecked = true
                    fragmentActivo = principalFragment
                }
            }
        })
        fragmentActivo = fragment
    }

    override fun onBackPressed() {
        val childFragmentManager = fragmentActivo.childFragmentManager
        // si ese fragment por dentro hizo cambios en el fragment entonces ir atras dentro de ese fragment
        if (childFragmentManager.backStackEntryCount > 0) {
            childFragmentManager.popBackStack()
            return
        } else {
            if (supportFragmentManager.backStackEntryCount > 0) {
                // ir al seccion anterior
                super.onBackPressed()
            } else {
                // Salir de la app si no queda nada en el stack
                finish()
            }
        }
    }

    private fun getIndex(tag: String?) : Int {
        return when (tag) {
            FRAGMENT_PRINCIPAL_TAG -> {
                FRAGMENT_PRINCIPAL_INDEX
            }
            FRAGMENT_CHATS_TAG -> {
                FRAGMENT_CHATS_INDEX
            }
            FRAGMENT_NOTIFICACIONES_TAG -> {
                FRAGMENT_NOTIFICACIONES_INDEX
            }
            FRAGMENT_PERFIL_TAG -> {
                FRAGMENT_PERFIL_INDEX
            }
            else -> FRAGMENT_PRINCIPAL_INDEX
        }
    }

    private fun getFragment(tag: String?) : SeccionFragment {
        return when (tag) {
            FRAGMENT_PRINCIPAL_TAG -> {
                principalFragment
            }
            FRAGMENT_CHATS_TAG -> {
                chatsFragment
            }
            FRAGMENT_NOTIFICACIONES_TAG -> {
                notificacionesFragment
            }
            FRAGMENT_PERFIL_TAG -> {
                perfilFragment
            }
            else -> principalFragment
        }
    }
}
