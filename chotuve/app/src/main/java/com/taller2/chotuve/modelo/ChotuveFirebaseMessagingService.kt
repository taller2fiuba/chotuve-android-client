package com.taller2.chotuve.modelo

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService

class ChotuveFirebaseMessagingService : FirebaseMessagingService() {
    // Singleton
    companion object {
        val instance = ChotuveFirebaseMessagingService()
    }

    private val firebaseDatabaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val RTDB_NODE = com.taller2.chotuve.BuildConfig.RTDB_NODE
    private val RTDB_NODE_NOTIFICACIONES = com.taller2.chotuve.BuildConfig.RTDB_NODE_NOTIFICACIONES
    protected val NOTIFICACIONES_CHILD = "$RTDB_NODE/$RTDB_NODE_NOTIFICACIONES"

    override fun onNewToken(token: String) {
        Log.d("Firebase Messaging", "Refreshed token: $token")
    }

    /**
     * Guarda en firebase rtdb el fmToken correspondiente al usuario logeado
     */
    fun asociarUsuarioAFMToken(usuarioId: Long) {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("Firebase Messaging", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                val token = task.result?.token!!
                firebaseDatabaseReference.child(NOTIFICACIONES_CHILD).child(usuarioId.toString()).setValue(token)
            })
    }

    fun desasociarUsuarioAFMToken(usuarioId: Long) {
        firebaseDatabaseReference.child(NOTIFICACIONES_CHILD).child(usuarioId.toString()).removeValue()
    }
}