package com.taller2.chotuve.vista.chats

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Mensaje
import com.taller2.chotuve.modelo.Modelo
import com.taller2.chotuve.modelo.Usuario
import com.taller2.chotuve.presentador.PresentadorMensajes
import com.taller2.chotuve.vista.SeccionFragment
import com.taller2.chotuve.vista.componentes.MensajeViewHolder
import kotlinx.android.synthetic.main.fragment_mensajes.*

class MensajesFragment(chatKey: String, val destinario: Usuario) : Fragment(), VistaMensajes {
    // TODO sacar hello-firebase
    val MENSAJES_CHILD = "hello-firebase/mensajes/$chatKey"
    private val MENSAJE_MIO = 0
    private val MENSAJE_OTRO = 1
    private val miUsuarioId = Modelo.instance.id
    private lateinit var firebaseDatabaseReference: DatabaseReference
    private lateinit var firebaseAdapter: FirebaseRecyclerAdapter<Mensaje, MensajeViewHolder>
    private val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(context)
    private val presentador = PresentadorMensajes(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mensajes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        top_bar_destinatario.title = destinario.email
        top_bar_destinatario.setOnClickListener {
            irAPerfilDeUsuario(destinario)
        }
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
        firebaseDatabaseReference = FirebaseDatabase.getInstance().reference
        ocultarCargandoSiChatAunNoExiste()
        configurarRecyclerView()
    }

    fun ocultarCargandoSiChatAunNoExiste() {
        firebaseDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.d("vista", error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.hasChild(MENSAJES_CHILD)) {
                    cargando_mensajes_barra_progreso.visibility = View.GONE
                }
            }
        })
    }

    private fun irAPerfilDeUsuario(usuario: Usuario) {
        val newFragment = SeccionFragment.perfil(usuario.id)
        val transicion = fragmentManager!!.beginTransaction().hide(this)
        transicion.add(R.id.fragment_container, newFragment)
        transicion.addToBackStack(null)
        transicion.commit()
    }

    private fun configurarRecyclerView() {
        linearLayoutManager.stackFromEnd = true
        mensajes_recycler_view.layoutManager = linearLayoutManager

        val mensajesRef: DatabaseReference = firebaseDatabaseReference.child(MENSAJES_CHILD)

        val options: FirebaseRecyclerOptions<Mensaje> =
            FirebaseRecyclerOptions.Builder<Mensaje>()
                .setQuery(mensajesRef.orderByChild("timestamp"), Mensaje::class.java)
                .build()
        firebaseAdapter =
            object : FirebaseRecyclerAdapter<Mensaje, MensajeViewHolder>(options) {
                override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MensajeViewHolder {
                    val inflater = LayoutInflater.from(viewGroup.context)
                    var viewHolder: MensajeViewHolder? = null
                    when (viewType) {
                        MENSAJE_MIO -> {
                            val viewMensajeMio: View =
                                inflater.inflate(R.layout.componente_mensaje_mio, viewGroup, false)
                            viewHolder =  MensajeViewHolder(viewMensajeMio)
                        }
                        MENSAJE_OTRO -> {
                            val viewMensajeOtro: View =
                                inflater.inflate(R.layout.componente_mensaje_otro, viewGroup, false)
                            viewHolder = MensajeViewHolder(viewMensajeOtro)
                        }
                    }
                    return viewHolder!!
                }

                override fun getItemViewType(position: Int): Int {
                    return if (getItem(position).enviadoPor == miUsuarioId) MENSAJE_MIO else MENSAJE_OTRO
                }

                override fun onBindViewHolder(
                    viewHolder: MensajeViewHolder,
                    position: Int,
                    mensaje: Mensaje
                ) {
                    cargando_mensajes_barra_progreso.visibility = View.GONE
                    viewHolder.setMensaje(mensaje)
                }
            }

        firebaseAdapter.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                val mensajeCantidad: Int = firebaseAdapter.itemCount
                val ultimoItemVisible: Int = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                // Si es la carga inicial del recycler o
                // estoy parado en el fondo de la lista
                // entonces ir al fondo de la lista y mostrar el nuevo mensaje
                if (ultimoItemVisible == -1 ||
                    positionStart >= mensajeCantidad - 1 &&
                    ultimoItemVisible == positionStart - 1
                ) {
                    mensajes_recycler_view.scrollToPosition(positionStart)
                }
            }
        })

        mensajes_recycler_view.adapter = firebaseAdapter
    }

    override fun onStart() {
        super.onStart()
        if (this::firebaseAdapter.isInitialized) {
            firebaseAdapter.startListening()
        }
    }

    override fun onPause() {
        if (this::firebaseAdapter.isInitialized) {
            firebaseAdapter.stopListening()
        }
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (this::firebaseAdapter.isInitialized) {
            firebaseAdapter.startListening()
        }
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
