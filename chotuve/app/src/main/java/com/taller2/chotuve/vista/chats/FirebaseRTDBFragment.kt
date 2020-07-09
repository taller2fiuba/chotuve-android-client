package com.taller2.chotuve.vista.chats

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.*
import com.taller2.chotuve.modelo.Modelo

open class FirebaseRTDBFragment : Fragment() {
    protected val miUsuarioId = Modelo.instance.id
    protected val RTDB_NODE = com.taller2.chotuve.BuildConfig.RTDB_NODE
    private val RTDB_NODE_CHATS = com.taller2.chotuve.BuildConfig.RTDB_NODE_CHATS
    protected val RTDB_NODE_MENSAJES = com.taller2.chotuve.BuildConfig.RTDB_NODE_MENSAJES
    protected val CHATS_CHILD = "$RTDB_NODE/$RTDB_NODE_CHATS/$miUsuarioId"

    protected val firebaseDatabaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
    protected lateinit var linearLayoutManager: LinearLayoutManager
    protected lateinit var firebaseAdapter: FirebaseRecyclerAdapter<Any?, RecyclerView.ViewHolder>

    protected fun ejecutarAccionSiChildNoExiste(child: String, accion: () -> Unit) {
        firebaseDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.d("vista", error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.hasChild(child)) {
                    accion()
                }
            }
        })
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
