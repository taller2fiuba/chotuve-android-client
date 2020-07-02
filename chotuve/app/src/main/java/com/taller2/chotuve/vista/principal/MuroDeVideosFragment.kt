package com.taller2.chotuve.vista.principal

import android.app.Activity.RESULT_OK
import android.content.Intent
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
import com.taller2.chotuve.vista.perfil.PerfilFragment
import com.taller2.chotuve.vista.scroll_infinito.ScrollInfinitoListener
import com.taller2.chotuve.vista.scroll_infinito.VideosAdapter
import com.taller2.chotuve.vista.ver_video.USUARIO_ID_KEY
import com.taller2.chotuve.vista.ver_video.VerVideoActivity

const val ID_KEY = "com.taller2.chotuve.ID_KEY"

class MuroDeVideosFragment : Fragment(), VistaPrincipal {
    private val presentador = PresentadorPrincipal(this, InteractorPrincipal())

    private lateinit var scrollInfinitoListener: ScrollInfinitoListener
    private lateinit var adapter: VideosAdapter
    private lateinit var videosView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_muro_de_videos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configurarRecyclerView()
        mostrarCargandoVideos()
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
        scrollInfinitoListener = object : ScrollInfinitoListener(linearLayoutManager) {
            override fun onLoadMore(pagina: Int, cantidadDeItems: Int, view: RecyclerView?) {
                // Se llama cuando hay que agregar nuevos videos a la vista
                videosView.post {
                    // esto hace que el cargando se ejecute justo despues de que esta funcion termine
                    // ahora no se puede agregar porque no podes agregar items al mismo tiempo que scrolleas
                    adapter.agregarCargando()
                }
                presentador.obtenerVideos(pagina)
            }
        }
        adapter = VideosAdapter(this)
        videosView.adapter = adapter
        videosView.addOnScrollListener(scrollInfinitoListener)
    }

    override fun mostrarVideos(videos: List<Video>) {
        ocultarCargandoVideos()
        adapter.sacarCargando()
        videosView.visibility = View.VISIBLE
        adapter.addAll(videos)
    }

    override fun setErrorRed() {
        Log.d("vista", "Error del server")
        Toast.makeText(context, "Error del server", Toast.LENGTH_LONG).show()
    }

    fun mostrarCargandoVideos() {
        if (view != null) {
            val cargando = view!!.findViewById<View>(R.id.cargando_video_barra_progreso) as ConstraintLayout
            cargando.visibility = View.VISIBLE
        }
    }

    fun ocultarCargandoVideos() {
        if (view != null) {
            val cargando = view!!.findViewById<View>(R.id.cargando_video_barra_progreso) as ConstraintLayout
            cargando.visibility = View.GONE
        }
    }

    fun scrollTop() {
        videosView.smoothScrollToPosition(0)
    }

    fun verVideo(id: String) {
        val intent = Intent(context, VerVideoActivity::class.java)
        intent.putExtra(ID_KEY, id)
        startActivityForResult(Intent.createChooser(intent, "Seleccionar imagen"), 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val usuarioId = data!!.getStringExtra(USUARIO_ID_KEY)
            val newFragment = PerfilFragment(usuarioId!!.toLong())
            val transaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.fragment_container, newFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}
