package com.taller2.chotuve.vista.chats

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Mensaje
import com.taller2.chotuve.modelo.Modelo
import com.taller2.chotuve.modelo.Usuario
import com.taller2.chotuve.presentador.PresentadorMensajes
import com.taller2.chotuve.util.cargarImagen
import com.taller2.chotuve.vista.SeccionFragment
import com.taller2.chotuve.vista.componentes.MensajeViewHolder
import com.taller2.chotuve.vista.scroll_infinito.MensajesAdapter
import kotlinx.android.synthetic.main.fragment_mensajes.*

class MensajesFragment(chatKey: String, val destinario: Usuario) : FirebaseRTDBFragment(), VistaMensajes {
    private val MENSAJES_CHILD = "$RTDB_NODE/$RTDB_NODE_MENSAJES/$chatKey"

    private val presentador = PresentadorMensajes(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mensajes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        top_bar_destinatario_email.text = destinario.email
        top_bar_destinatario_container.setOnClickListener {
            irAPerfilDeUsuario(destinario)
        }
        cargarImagen(destinario, top_bar_destinatario_foto, this)
        top_bar_destinatario.setNavigationOnClickListener {
            fragmentManager!!.popBackStack()
        }
        boton_enviar_mensaje.setOnClickListener {
            val nuevoMensaje = nuevo_mensaje_contenido.text?.toString()
            when {
                nuevoMensaje.isNullOrEmpty() -> nuevo_mensaje_contenido.error = "No puede estar vacío"
                else -> {
                    boton_enviar_mensaje.visibility = View.GONE
                    creando_mensaje_barra_progreso.visibility = View.VISIBLE
                    presentador.enviarMensaje(destinario.id, nuevoMensaje)
                }
            }
        }
        ocultarCargandoSiChatAunNoExiste()
        configurarRecyclerView()
    }

    fun ocultarCargandoSiChatAunNoExiste() {
        ejecutarAccionSiChildNoExiste(MENSAJES_CHILD) {
            cargando_mensajes_barra_progreso.visibility = View.GONE
        }
    }

    private fun irAPerfilDeUsuario(usuario: Usuario) {
        val newFragment = SeccionFragment.perfil(usuario.id)
        val transicion = fragmentManager!!.beginTransaction().hide(this)
        transicion.add(R.id.fragment_container, newFragment)
        transicion.addToBackStack(null)
        transicion.commit()
    }

    private fun configurarRecyclerView() {
        linearLayoutManager = LinearLayoutManager(context)
        mensajes_recycler_view.layoutManager = linearLayoutManager
        linearLayoutManager.stackFromEnd = true

        val mensajesRef: DatabaseReference = firebaseDatabaseReference.child(MENSAJES_CHILD)

        val options: FirebaseRecyclerOptions<Mensaje> =
            FirebaseRecyclerOptions.Builder<Mensaje>()
                .setQuery(mensajesRef.orderByChild("timestamp"), Mensaje::class.java)
                .build()

        firebaseAdapter = MensajesAdapter(this, miUsuarioId!!, options) as FirebaseRecyclerAdapter<Any?, RecyclerView.ViewHolder>

        firebaseAdapter.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                val mensajeCantidad: Int = firebaseAdapter.itemCount
                val ultimoItemVisible: Int = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                // Si es la carga inicial del recycler o
                // estoy parado en el fondo de la lista
                // entonces ir al nuevo fondo de la lista para mostrar el nuevo mensaje
                if (ultimoItemVisible == -1 ||
                    positionStart >= mensajeCantidad - 1 &&
                    ultimoItemVisible == positionStart - 1
                ) {
                    mensajes_recycler_view.scrollToPosition(positionStart)
                }
            }
        })

        mensajes_recycler_view.adapter = firebaseAdapter
        firebaseAdapter.startListening()
    }

    fun mensajesCargados() {
        cargando_mensajes_barra_progreso.visibility = View.GONE
    }

    override fun mensajeEnviado() {
        nuevo_mensaje_contenido.setText("")
        creando_mensaje_barra_progreso.visibility = View.GONE
        boton_enviar_mensaje.visibility = View.VISIBLE
    }

    override fun setErrorRed() {
        creando_mensaje_barra_progreso.visibility = View.GONE
        boton_enviar_mensaje.visibility = View.VISIBLE
    }
}
