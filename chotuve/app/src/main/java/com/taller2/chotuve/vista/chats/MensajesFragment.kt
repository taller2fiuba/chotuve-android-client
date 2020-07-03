package com.taller2.chotuve.vista.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.Mensaje
import com.taller2.chotuve.vista.componentes.MensajeViewHolder
import kotlinx.android.synthetic.main.fragment_mensajes.*

class MensajesFragment(chatKey: String) : Fragment() {
    val MENSAJES_CHILD = "hello-firebase/mensajes/$chatKey"
    private lateinit var firebaseDatabaseReference: DatabaseReference
    private lateinit var firebaseAdapter: FirebaseRecyclerAdapter<Mensaje, MensajeViewHolder>
    private val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(context)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mensajes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configurarRecyclerView()
    }

    private fun configurarRecyclerView() {
        linearLayoutManager.stackFromEnd = true
        mensajes_recycler_view.layoutManager = linearLayoutManager
        firebaseDatabaseReference = FirebaseDatabase.getInstance().reference

        val mensajesRef: DatabaseReference = firebaseDatabaseReference.child(MENSAJES_CHILD)

        val options: FirebaseRecyclerOptions<Mensaje> =
            FirebaseRecyclerOptions.Builder<Mensaje>()
                .setQuery(mensajesRef, Mensaje::class.java)
                .build()
        firebaseAdapter =
            object : FirebaseRecyclerAdapter<Mensaje, MensajeViewHolder>(options) {
                override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MensajeViewHolder {
                    val inflater = LayoutInflater.from(viewGroup.context)
                    return MensajeViewHolder(
                        inflater.inflate(
                            R.layout.componente_mensaje,
                            viewGroup,
                            false
                        )
                    )
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
        firebaseAdapter.startListening()
    }

    override fun onPause() {
        firebaseAdapter.stopListening()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        firebaseAdapter.startListening()
    }

}
