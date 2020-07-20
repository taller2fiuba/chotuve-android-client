package com.taller2.chotuve.vista.contactos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Usuario
import com.taller2.chotuve.presentador.PresentadorContactos
import com.taller2.chotuve.vista.componentes.UsuarioView
import kotlinx.android.synthetic.main.fragment_contactos.*

class ContactosFragment(private val fm: FragmentManager, private val mostrarSolicitudes: Boolean, private val clickListener: (Usuario) -> Int) : Fragment(), VistaContactos {
    private val presentador = PresentadorContactos(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contactos, container, false)  as View
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (mostrarSolicitudes) {
            boton_ver_solicitudes.setOnClickListener {
                val newFragment = SolicitudesDeContactoFragment()
                val transicion = fm.beginTransaction()
                transicion.replace(R.id.fragment_container, newFragment)
                transicion.addToBackStack(null)
                transicion.commit()
            }
        } else {
            boton_ver_solicitudes_divider.visibility = View.GONE
            boton_ver_solicitudes.visibility = View.GONE
        }
        presentador.obtenerContactos()
    }

    override fun mostrarContactos(contactos: List<Usuario>) {
        val contactosOrdenados = contactos.sortedWith(compareBy { it.email })
        contactosOrdenados.forEach { usuario: Usuario ->
            val usuarioView = UsuarioView(context!!)
            usuarioView.setUsuario(usuario)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 8, 0, 8)
            usuarioView.layoutParams = params
            usuarioView.setOnClickListener {
                clickListener(usuario)
            }
            contactos_container.addView(usuarioView)
        }
        cargando_contactos_barra_progreso.visibility = View.GONE
        contactos_view.visibility = View.VISIBLE
        if (contactosOrdenados.isEmpty()) {
            aun_no_tenes_contactos.visibility = View.VISIBLE
        }
    }

    override fun setErrorRed() {
        Log.d("vista", "Error del server")
        Toast.makeText(context, "Error del server", Toast.LENGTH_LONG).show()
    }

}
