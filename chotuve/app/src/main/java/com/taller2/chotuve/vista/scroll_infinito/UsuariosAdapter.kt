package com.taller2.chotuve.vista.scroll_infinito

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Usuario
import com.taller2.chotuve.vista.componentes.UsuarioView
import com.taller2.chotuve.vista.contactos.ContactosFragment
import java.util.*


class UsuariosAdapter(context: Context, val fragment: ContactosFragment) : ArrayAdapter<Usuario>(context, R.layout.usuario_list),
    Filterable {
    private var usuarios = listOf<Usuario>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val usuario: Usuario? = getItem(position)
        var listaView = convertView
        if (listaView == null) {
            listaView = LayoutInflater.from(context).inflate(R.layout.usuario_list, parent, false)
        }
        val usuarioView = listaView!!.findViewById<View>(R.id.usuario) as UsuarioView
        usuarioView.setUsuario(usuario!!)
        usuarioView.setOnClickListener {
            fragment.clickUsuario(usuario)
        }
        return listaView
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(
                constraint: CharSequence,
                results: FilterResults
            ) {
                clear()
                addAll(results.values as List<Usuario>)
                notifyDataSetChanged()
            }

            override fun performFiltering(constraint: CharSequence): FilterResults {
                var constraint = constraint
                val results = FilterResults()

                constraint = constraint.toString().toLowerCase()
                val filtrados = usuarios.filter {
                    it.email.toLowerCase(Locale.ROOT).startsWith(constraint.toString())
                }
                results.count = filtrados.size
                results.values = filtrados
                return results
            }
        }
    }

    fun setUsuarios(lista : List<Usuario>) {
        addAll(lista)
        usuarios = lista
    }
}