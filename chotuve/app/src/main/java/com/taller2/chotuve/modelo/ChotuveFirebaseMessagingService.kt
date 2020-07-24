package com.taller2.chotuve.modelo

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.taller2.chotuve.R
import com.taller2.chotuve.vista.MainActivity

class ChotuveFirebaseMessagingService : FirebaseMessagingService() {
    // Singleton
    companion object {
        val instance = ChotuveFirebaseMessagingService()
        const val INTENT_ACTION_RECARGAR_CHATS_SIN_LEER = "INTENT_ACTION_RECARGAR_CHATS_SIN_LEER"
    }

    private val firebaseDatabaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val RTDB_NODE = com.taller2.chotuve.BuildConfig.RTDB_NODE
    private val RTDB_NODE_NOTIFICACIONES = com.taller2.chotuve.BuildConfig.RTDB_NODE_NOTIFICACIONES
    private val NOTIFICACIONES_CHILD = "$RTDB_NODE/$RTDB_NODE_NOTIFICACIONES"
    private val modelo = Modelo.instance

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        super.onMessageReceived(remoteMessage)

        // TODO pasar a algun codigo de mensaje
        if (remoteMessage.notification!!.title!!.contains("Nuevo mensaje de")) {
            modelo.mensajesSinLeer++
            val intent = Intent().apply {
                action = INTENT_ACTION_RECARGAR_CHATS_SIN_LEER
            }
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        }

        //sendNotification(remoteMessage.notification!!)
    }

    // armar notificacion a mano, no se usa por ahora
    private fun sendNotification(notificacion: RemoteMessage.Notification) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT)

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo_sin_nombre)
            .setContentTitle(notificacion.title!!)
            .setContentText(notificacion.body!!)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setPriority(2)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Chotuve notificaciones Channel",
                NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(notificacion.hashCode(), notificationBuilder.build())
    }

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