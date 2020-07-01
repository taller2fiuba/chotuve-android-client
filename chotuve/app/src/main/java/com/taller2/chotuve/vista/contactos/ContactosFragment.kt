package com.taller2.chotuve.vista.contactos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.textview.MaterialTextView
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Autor
import com.taller2.chotuve.presentador.PresentadorContactos
import com.taller2.chotuve.vista.perfil.PerfilFragment
import kotlinx.android.synthetic.main.fragment_contactos.*

class ContactosFragment(val perfilFragment: PerfilFragment) : Fragment(), VistaContactos {
    companion object {
        fun newInstance(perfilFragment: PerfilFragment): ContactosFragment =
            ContactosFragment(perfilFragment)
    }

    private val presentador = PresentadorContactos(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contactos, container, false)  as View
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        boton_ver_solicitudes.setOnClickListener {
            val newFragment = SolicitudesDeContactoFragment()
            val transicion = activity!!.supportFragmentManager.beginTransaction().hide(perfilFragment)
            transicion.add(R.id.container_navegacion, newFragment)
            transicion.addToBackStack(null)
            transicion.commit()
        }
        presentador.obtenerContactos()
    }

    override fun mostrarContactos(contactos: List<Autor>) {
        // TODO esto se rompe si cambias de pestaña antes de que carge aveces, no puedo determinar bien porque aveces apsa y aveces no
        contactos.forEach { autor: Autor ->
            val textView = MaterialTextView(context!!)
            textView.setTextAppearance(android.R.style.TextAppearance_Material_Body1)
            textView.text = autor.email
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 8, 0, 8)
            textView.layoutParams = params
            textView.setOnClickListener {
                irAPerfilDeUsuario(autor.usuarioId)
            }
            contactos_container.addView(textView)
        }
        cargando_contactos_barra_progreso.visibility = View.GONE
        contactos_container.visibility = View.VISIBLE
    }

    fun irAPerfilDeUsuario(usuarioId: Long) {
        val newFragment =
            PerfilFragment(usuarioId)
        val transicion = activity!!.supportFragmentManager.beginTransaction().hide(perfilFragment)
        transicion.add(R.id.container_navegacion, newFragment)
        transicion.addToBackStack(null)
        transicion.commit()
    }

    override fun setErrorRed() {
        Log.d("vista", "Error del server")
        Toast.makeText(context, "Error del server", Toast.LENGTH_LONG).show()
    }

}
