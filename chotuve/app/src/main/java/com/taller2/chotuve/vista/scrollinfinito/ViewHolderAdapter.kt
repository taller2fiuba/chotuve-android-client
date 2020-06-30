package com.taller2.chotuve.vista.scrollinfinito

import androidx.recyclerview.widget.RecyclerView

abstract class ViewHolderAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var elementos: MutableList<Any?> = mutableListOf()
    var estaCargando = false

    fun agregarCargando() {
        estaCargando = true
        add(null)
    }

    fun sacarCargando() {
        if (estaCargando) {
            estaCargando = false
            val position: Int = elementos.size - 1
            elementos.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int {
        return elementos.size
    }

    fun getItem(position: Int): Any? {
        return elementos[position]
    }

    fun add(elemento: Any?) {
        elementos.add(elemento)
        notifyItemInserted(elementos.size - 1)
    }

    fun add(position: Int, elemento: Any?) {
        elementos.add(position, elemento)
        notifyItemInserted(position)
    }

    fun addAll(elementos: List<Any>) {
        for (elemento in elementos) {
            add(elemento)
        }
    }

    fun remove(elemento: Any) {
        val position: Int = elementos.indexOf(elemento)
        if (position > -1) {
            elementos.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clear() {
        while (itemCount > 0) {
            remove(getItem(0)!!)
        }
    }

    fun isEmpty(): Boolean {
        return itemCount == 0
    }
}