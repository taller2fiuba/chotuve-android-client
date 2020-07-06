package com.taller2.chotuve.vista.chats

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.database.SnapshotParser
import com.google.firebase.database.*
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Chat
import com.taller2.chotuve.modelo.Modelo
import com.taller2.chotuve.modelo.Usuario
import com.taller2.chotuve.presentador.PresentadorContactos
import com.taller2.chotuve.vista.componentes.ChatViewHolder
import com.taller2.chotuve.vista.contactos.VistaContactos
import kotlinx.android.synthetic.main.fragment_chats.*


class ChatsFragment : Fragment(), VistaContactos {
    // TODO mucho codigo repetido con mensajes fragment y con videos adapter
    private val miUsuarioId = Modelo.instance.id
    val CHATS_CHILD = "hello-firebase/chats/$miUsuarioId"
    private val presentadorContactos = PresentadorContactos(this)
    private lateinit var contactos: Map<Long, Usuario>
    private lateinit var firebaseDatabaseReference: DatabaseReference
    private lateinit var firebaseAdapter: FirebaseRecyclerAdapter<Chat, ChatViewHolder>
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        linearLayoutManager = LinearLayoutManager(context)
        presentadorContactos.obtenerContactos()
    }

    override fun mostrarContactos(contactos: List<Usuario>) {
        this.contactos = contactos.map { it.id to it }.toMap()
        firebaseDatabaseReference = FirebaseDatabase.getInstance().reference
        mostrarSinChatsSiAunNoHayChats()
        configurarRecyclerView()
        firebaseAdapter.startListening()
    }

    fun mostrarSinChatsSiAunNoHayChats() {
        firebaseDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.hasChild(CHATS_CHILD)) {
                    mensaje_sin_chats.visibility = View.VISIBLE
                    cargando_chats_barra_progreso.visibility = View.GONE
                }
            }
        })
    }

    override fun setErrorRed() {
        Log.d("vista", "Error del server")
        Toast.makeText(context, "Error del server", Toast.LENGTH_LONG).show()
    }


    private fun configurarRecyclerView() {
        chats_recycler_view.layoutManager = linearLayoutManager

        val chatsRef: DatabaseReference = firebaseDatabaseReference.child(CHATS_CHILD)

        val parser =
            SnapshotParser<Chat> { dataSnapshot ->
                val chat: Chat = dataSnapshot.getValue(Chat::class.java)!!
                val destinatarioId = dataSnapshot.key!!.toLong()
                if (destinatarioId > miUsuarioId!!) {
                    chat.key = "$miUsuarioId-$destinatarioId"
                } else {
                    chat.key = "$destinatarioId-$miUsuarioId"
                }
                chat.destinatario = contactos[destinatarioId]
                chat
            }

        val options: FirebaseRecyclerOptions<Chat> =
            FirebaseRecyclerOptions.Builder<Chat>()
                .setQuery(chatsRef.orderByChild("orden"), parser)
                .build()
        firebaseAdapter =
            object : FirebaseRecyclerAdapter<Chat, ChatViewHolder>(options) {
                override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ChatViewHolder {
                    val inflater = LayoutInflater.from(viewGroup.context)
                    return ChatViewHolder(
                        inflater.inflate(
                            R.layout.componente_chat,
                            viewGroup,
                            false
                        )
                    )
                }

                override fun onBindViewHolder(
                    viewHolder: ChatViewHolder,
                    position: Int,
                    chat: Chat
                ) {
                    mensaje_sin_chats.visibility = View.GONE
                    cargando_chats_barra_progreso.visibility = View.GONE
                    viewHolder.setChat(chat)
                    viewHolder.clickListener = object : ChatViewHolder.Clicklistener {
                        override fun onItemClick(view: View?, position: Int) {
                            val chat = getItem(position)
                            verMensajes(chat.key!!, chat.destinatario!!)
                        }
                    }
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
                    chats_recycler_view.scrollToPosition(positionStart)
                }
            }
        })

        chats_recycler_view.adapter = firebaseAdapter
    }

    private fun verMensajes(key: String, destinario: Usuario) {
        val newFragment = MensajesFragment(key, destinario)
        val transaction = fragmentManager!!.beginTransaction().hide(this)
        transaction.add(R.id.fragment_container, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
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

}
