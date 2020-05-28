package com.taller2.chotuve.vista

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.taller2.chotuve.vista.perfil.PerfilFragment
import com.taller2.chotuve.vista.principal.PrincipalFragment
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Modelo
import com.taller2.chotuve.vista.autenticacion.RegistrarmeActivity
import com.taller2.chotuve.vista.chats.ChatsFragment
import com.taller2.chotuve.vista.notificaciones.NotificacionesFragment


class MainActivity : AppCompatActivity() {

    lateinit var navegacion : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!Modelo.instance.estaLogueado())
            startActivity(Intent(this, RegistrarmeActivity::class.java))
        else {
            setContentView(R.layout.home)

            navegacion = findViewById(R.id.navegacion)
            navegacion.setOnNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.principal_menu -> {
                        val fragment =
                            PrincipalFragment.newInstance()
                        openFragment(fragment)
                        true
                    }
                    R.id.notificaciones_menu -> {
                        val fragment =
                            NotificacionesFragment.newInstance()
                        openFragment(fragment)
                        true
                    }
                    R.id.chats_menu -> {
                        val fragment = ChatsFragment.newInstance()
                        openFragment(fragment)
                        true
                    }
                    R.id.perfil_menu -> {
                        val fragment =
                            PerfilFragment.newInstance()
                        openFragment(fragment)
                        true
                    }
                    else -> false
                }
            }
            navegacion.selectedItemId = R.id.principal_menu
        }
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container_navegacion, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


}
