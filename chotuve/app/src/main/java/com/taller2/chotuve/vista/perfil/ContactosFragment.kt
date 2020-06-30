package com.taller2.chotuve.vista.perfil

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Autor
import com.taller2.chotuve.presentador.PresentadorContactos
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
        presentador.obtenerContactos()
    }

    override fun mostrarContactos(contactos: List<Autor>) {
        // TODO esto se rompe si cambias de pestaña antes de que carge aveces, no puedo determinar bien porque aveces apsa y aveces no
        contactos.forEach { autor: Autor ->
            val textView = TextView(context)
            textView.text = autor.email
            textView.setOnClickListener {
                irAPerfilDeUsuario(autor.usuarioId)
            }
            contactos_container.addView(textView)
            val divider = View(context)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                1
            )
            params.setMargins(0, 8, 0, 8)
            divider.layoutParams = params
            divider.setBackgroundColor(ContextCompat.getColor(context!!, android.R.color.darker_gray))
            contactos_container.addView(divider)
        }
        cargando_contactos_barra_progreso.visibility = View.GONE
        contactos_container.visibility = View.VISIBLE
    }

    fun irAPerfilDeUsuario(usuarioId: Long) {
        val newFragment = PerfilFragment(usuarioId)
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
