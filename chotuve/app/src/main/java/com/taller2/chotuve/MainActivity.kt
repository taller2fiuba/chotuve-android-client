package com.taller2.chotuve

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    lateinit var navegacion : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // solo si no hay token guardado
        // startActivity(Intent(this, RegistrarmeActivity::class.java))
        // else: lo que viene aca abajo
        setContentView(R.layout.home)

        navegacion = findViewById(R.id.navegacion)
        navegacion.selectedItemId = R.id.principal_menu
        navegacion.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.principal_menu -> {
                    val fragment = PrincipalFragment.newInstance()
                    openFragment(fragment)
                    true
                }
                R.id.notificaciones_menu -> {
                    val fragment = NotificacionesFragment.newInstance()
                    openFragment(fragment)
                    true
                }
                R.id.chats_menu -> {
                    val fragment = ChatsFragment.newInstance()
                    openFragment(fragment)
                    true
                }
                R.id.perfil_menu -> {
                    val fragment = PerfilFragment.newInstance()
                    openFragment(fragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container_navegacion, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


}
