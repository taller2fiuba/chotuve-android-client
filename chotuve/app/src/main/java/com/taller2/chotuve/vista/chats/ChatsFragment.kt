package com.taller2.chotuve.vista.chats

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.database.SnapshotParser
import com.google.firebase.database.*
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Chat
import com.taller2.chotuve.modelo.Usuario
import com.taller2.chotuve.presentador.PresentadorContactos
import com.taller2.chotuve.vista.contactos.ContactosFragment
import com.taller2.chotuve.vista.contactos.VistaContactos
import com.taller2.chotuve.vista.scroll_infinito.ChatsAdapter
import kotlinx.android.synthetic.main.fragment_chats.*


class ChatsFragment : FirebaseRTDBFragment(), VistaContactos {
    private val presentadorContactos = PresentadorContactos(this)

    private lateinit var contactos: Map<Long, Usuario>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chats, container, false)
        recyclerView = chats_recycler_view
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presentadorContactos.obtenerContactos()
    }

    override fun mostrarContactos(contactos: List<Usuario>) {
        this.contactos = contactos.map { it.id to it }.toMap()
        mostrarSinChatsSiAunNoHayChats()
        nuevo_chat_boton.setOnClickListener {
            irACrearChat()
        }
        configurarRecyclerView()
    }

    fun mostrarSinChatsSiAunNoHayChats() {
        ejecutarAccionSiChildNoExiste(CHATS_CHILD) {
            mensaje_sin_chats.visibility = View.VISIBLE
            cargando_chats_barra_progreso.visibility = View.GONE
        }
    }

    override fun setErrorRed() {
        Log.d("vista", "Error del server")
        Toast.makeText(context, "Error del server", Toast.LENGTH_LONG).show()
    }

    private fun configurarRecyclerView() {
        val chatsRef: DatabaseReference = firebaseDatabaseReference.child(CHATS_CHILD)

        val parser =
            SnapshotParser { dataSnapshot ->
                val chat: Chat = dataSnapshot.getValue(Chat::class.java)!!
                val destinatarioId = dataSnapshot.key!!.toLong()
                chat.key = generarKey(destinatarioId)
                chat.destinatario = contactos[destinatarioId]
                chat
            }

        val options: FirebaseRecyclerOptions<Chat> =
            FirebaseRecyclerOptions.Builder<Chat>()
                .setQuery(chatsRef.orderByChild("orden"), parser)
                .build()
        firebaseAdapter = ChatsAdapter(this, options) as FirebaseRecyclerAdapter<Any?, RecyclerView.ViewHolder>
        recyclerView.adapter = firebaseAdapter
        firebaseAdapter.startListening()
    }

    private fun generarKey(usuarioId: Long): String {
        return if (usuarioId > miUsuarioId!!) {
            "$miUsuarioId-$usuarioId"
        } else {
            "$usuarioId-$miUsuarioId"
        }
    }

    fun verMensajes(key: String, destinario: Usuario) {
        val newFragment = MensajesFragment(key, destinario)
        val transaction = fragmentManager!!.beginTransaction().hide(this)
        transaction.add(R.id.fragment_container, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun irACrearChat() {
        val contactosFragment = ContactosFragment(fragmentManager!!, false) { usuario: Usuario ->
            // cuando toco en un contacto ir a los mensajes con ese contacto
            fragmentManager!!.popBackStack()
            val mensajesFragment = MensajesFragment(generarKey(usuario.id), usuario)
            val transaction = fragmentManager!!.beginTransaction().hide(this)
            transaction.add(R.id.fragment_container, mensajesFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        val transaction = fragmentManager!!.beginTransaction().hide(this)
        transaction.add(R.id.fragment_container, contactosFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun chatsCargados() {
        mensaje_sin_chats.visibility = View.GONE
        cargando_chats_barra_progreso.visibility = View.GONE
    }
}
