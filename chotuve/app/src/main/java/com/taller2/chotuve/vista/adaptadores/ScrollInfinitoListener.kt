package com.taller2.chotuve.vista.adaptadores

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager


abstract class ScrollInfinitoListener : RecyclerView.OnScrollListener {
    // https://github.com/codepath/android_guides/wiki/Endless-Scrolling-with-AdapterViews-and-RecyclerView

    // La cantidad minima de items cargados sin ver antes de ir a buscar la siguiente pagina.
    private var visibleThreshold = 5

    private var paginaActual = 0

    // la cantidad de items que habia antes de la ultima carga
    private var cantidadDeItemsAnterior = 0

    // True si estoy esperando que cargen mas items
    private var cargando = true

    // la pagina inicial
    private val paginaInicial = 0
    private var mLayoutManager: RecyclerView.LayoutManager

    constructor(layoutManager: LinearLayoutManager) {
        mLayoutManager = layoutManager
    }

    constructor(layoutManager: GridLayoutManager) {
        mLayoutManager = layoutManager
        visibleThreshold *= layoutManager.spanCount
    }

    constructor(layoutManager: StaggeredGridLayoutManager) {
        mLayoutManager = layoutManager
        visibleThreshold *= layoutManager.spanCount
    }

    // Se llama cuando se scrollea la pagina
    // Chequeo si llegue al limite para cargar mas items si es que ya no los estoy cargando
    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        var posicionDelUltimoItemVisible = 0
        val cantidadDeItemsActual = mLayoutManager.itemCount
        if (mLayoutManager is StaggeredGridLayoutManager) {
            val posicionesDelUltimoItemVisible =
                (mLayoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(null)
            
            posicionDelUltimoItemVisible = posicionesDelUltimoItemVisible.max() ?: 0
        } else if (mLayoutManager is GridLayoutManager) {
            posicionDelUltimoItemVisible =
                (mLayoutManager as GridLayoutManager).findLastVisibleItemPosition()
        } else if (mLayoutManager is LinearLayoutManager) {
            posicionDelUltimoItemVisible =
                (mLayoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        }

        if (cantidadDeItemsActual < cantidadDeItemsAnterior) {
            paginaActual = paginaInicial
            cantidadDeItemsAnterior = cantidadDeItemsActual
            // si vuelvo a tengo que volver a cargar la pagina para mostrar algo
            if (cantidadDeItemsActual == 0) {
                cargando = true
            }
        }
        // Si esta cargando pero hay items nuevos, entonces termino de cargar
        // + 1 por el items que se agrega para mostrar la barra de prograso
        if (cargando && cantidadDeItemsActual > cantidadDeItemsAnterior + 1) {
            cargando = false
            cantidadDeItemsAnterior = cantidadDeItemsActual
        }

        // Si no estoy cargando y supere el limite de items que quedan sin leer entonces cargo mas items
        if (!cargando && posicionDelUltimoItemVisible + visibleThreshold > cantidadDeItemsActual) {
            paginaActual++
            cargando = true
            onLoadMore(paginaActual, cantidadDeItemsActual, view)
        }
    }

    // Vuelve a poner el listener en su estado inicial
    fun resetState() {
        paginaActual = paginaInicial
        cantidadDeItemsAnterior = 0
        cargando = true
    }

    // Define como se obtiene la siguiente pagina
    abstract fun onLoadMore(pagina: Int, cantidadDeItems: Int, view: RecyclerView?)
}