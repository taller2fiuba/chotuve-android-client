package com.taller2.chotuve.vista.principal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Video
import com.taller2.chotuve.modelo.interactor.InteractorPrincipal
import com.taller2.chotuve.presentador.PresentadorPrincipal
import com.taller2.chotuve.vista.adaptadores.EndlessRecyclerViewScrollListener
import com.taller2.chotuve.vista.adaptadores.VideosAdapter


class PrincipalFragment : Fragment(), VistaPrincipal {
    private val presentador = PresentadorPrincipal(this, InteractorPrincipal())

    private var scrollListener: EndlessRecyclerViewScrollListener? = null
    private lateinit var adapter: VideosAdapter
    private lateinit var videosView: RecyclerView

    companion object {
        fun newInstance(): PrincipalFragment =
            PrincipalFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_principal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configurarRecyclerView()
        presentador.obtenerVideos(0)
    }

    override fun onDestroy() {
        super.onDestroy()
        presentador.onDestroy()
    }

    fun configurarRecyclerView() {
        videosView = view!!.findViewById<View>(R.id.videos_recycler_view) as RecyclerView
        val linearLayoutManager = LinearLayoutManager(context)
        videosView.layoutManager = linearLayoutManager
        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(offset: Int, totalItemsCount: Int, view: RecyclerView?) {
                // Se llama cuando hay que agregar nuevos videos a la vista
                presentador.obtenerVideos(offset)
            }
        }
        adapter = VideosAdapter()
        videosView.adapter = adapter
        videosView.addOnScrollListener(scrollListener as EndlessRecyclerViewScrollListener)
    }

    override fun mostrarVideos(videos: List<Video>) {
        ocultarCargandoVideo()
        videosView.visibility = View.VISIBLE
        adapter.addAll(videos)
    }

    override fun setErrorRed() {
        Log.d("vista", "Error del server")
        Toast.makeText(context, "Error del server", Toast.LENGTH_LONG).show()
    }

    override fun mostrarCargandoVideo() {
        val cargando = view!!.findViewById<View>(R.id.cargando_video_barra_progreso) as ConstraintLayout
        cargando.visibility = View.VISIBLE
    }

    fun ocultarCargandoVideo() {
        val cargando = view!!.findViewById<View>(R.id.cargando_video_barra_progreso) as ConstraintLayout
        cargando.visibility = View.GONE
    }

}
