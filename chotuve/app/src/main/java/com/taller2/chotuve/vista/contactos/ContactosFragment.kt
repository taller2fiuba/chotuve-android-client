package com.taller2.chotuve.vista.contactos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.material.textview.MaterialTextView
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Usuario
import com.taller2.chotuve.presentador.PresentadorContactos
import com.taller2.chotuve.vista.perfil.PerfilFragment
import kotlinx.android.synthetic.main.fragment_contactos.*

class ContactosFragment(private val fragmentAnterior: Fragment) : Fragment(), VistaContactos {
    companion object {
        fun newInstance(fragmentAnterior: Fragment): ContactosFragment =
            ContactosFragment(fragmentAnterior)
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
            val transicion = activity!!.supportFragmentManager.beginTransaction()
            transicion.replace(R.id.container_navegacion, newFragment)
            transicion.addToBackStack(null)
            transicion.commit()
        }
        presentador.obtenerContactos()
    }

    override fun mostrarContactos(contactos: List<Usuario>) {
        contactos.forEach { usuario: Usuario ->
            val textView = MaterialTextView(context!!)
            // TODO este estilo no esta bien, pasar a component
            textView.setTextAppearance(android.R.style.TextAppearance_Material_Body1)
            textView.text = usuario.email
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 8, 0, 8)
            textView.layoutParams = params
            textView.setOnClickListener {
                irAPerfilDeUsuario(usuario.id)
            }
            contactos_container.addView(textView)
        }
        cargando_contactos_barra_progreso.visibility = View.GONE
        contactos_container.visibility = View.VISIBLE
        if (contactos.isEmpty()) {
            aun_no_tenes_contactos.visibility = View.VISIBLE
        }
    }

    fun irAPerfilDeUsuario(usuarioId: Long) {
        val newFragment =
            PerfilFragment(usuarioId)
        val transicion = activity!!.supportFragmentManager.beginTransaction().hide(fragmentAnterior)
        transicion.add(R.id.container_navegacion, newFragment)
        transicion.addToBackStack(null)
        transicion.commit()
    }

    override fun setErrorRed() {
        Log.d("vista", "Error del server")
        Toast.makeText(context, "Error del server", Toast.LENGTH_LONG).show()
    }

}
