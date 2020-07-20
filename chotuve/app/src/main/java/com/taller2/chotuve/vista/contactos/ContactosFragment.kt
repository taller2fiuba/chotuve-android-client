package com.taller2.chotuve.vista.contactos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Usuario
import com.taller2.chotuve.presentador.PresentadorContactos
import com.taller2.chotuve.vista.scroll_infinito.UsuariosAdapter
import kotlinx.android.synthetic.main.fragment_contactos.*


class ContactosFragment(private val fm: FragmentManager, private val mostrarSolicitudes: Boolean, private val clickListener: (Usuario) -> Int) : Fragment(), VistaContactos {
    private val presentador = PresentadorContactos(this)
    private lateinit var adapter : UsuariosAdapter

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
        configurarAdapter()
        configurarRefreshLayout()
        presentador.obtenerContactos()
    }

    private fun configurarAdapter() {
        adapter = UsuariosAdapter(context!!, this)
        contactos_list.adapter = adapter
        buscar_contactos.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })
    }

    private fun configurarRefreshLayout() {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorSecondary)
        swipeRefreshLayout.setOnRefreshListener {
            adapter.clear()
            presentador.obtenerContactos()
        }
    }


    override fun mostrarContactos(contactos: List<Usuario>) {
        val contactosOrdenados = contactos.sortedBy { it.email }
        adapter.setUsuarios(contactosOrdenados)
        cargando_contactos_barra_progreso.visibility = View.GONE
        swipeRefreshLayout.isRefreshing = false
        contactos_view.visibility = View.VISIBLE
        if (contactosOrdenados.isEmpty()) {
            aun_no_tenes_contactos.visibility = View.VISIBLE
        }
    }

    override fun setErrorRed() {
        Log.d("vista", "Error del server")
        Toast.makeText(context, "Error del server", Toast.LENGTH_LONG).show()
    }

    fun clickUsuario(usuario: Usuario) {
        clickListener(usuario)
    }
}
