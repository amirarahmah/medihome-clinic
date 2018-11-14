package com.example.asus.medihome_clinic.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.example.asus.medihome_clinic.ui.MainActivity
import com.example.asus.medihome_clinic.R
import com.example.asus.medihome_clinic.util.PreferenceHelper
import com.example.asus.medihome_clinic.util.PreferenceHelper.set
import com.example.asus.medihome_clinic.util.PreferenceHelper.get
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {


    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        sendNotification(remoteMessage)
    }

    override fun onNewToken(token: String?) {
        Log.d(TAG, "Refreshed token: $token")

        val prefs = PreferenceHelper.defaultPrefs(this)
        prefs["firebaseToken"] = token
        prefs["tokenSended"] = false

        val currentUser = FirebaseAuth.getInstance().currentUser
        if(currentUser != null){
            sendTokenToServer()
        }

    }

    private fun sendTokenToServer() {
        Log.d(TAG, "send token to server")
        val prefs = PreferenceHelper.defaultPrefs(this)

        if (!prefs["tokenSended", false]!!) {
            val token: String? = prefs["firebaseToken"]
            val userId = FirebaseAuth.getInstance().currentUser?.uid

            val userRef = FirebaseDatabase
                    .getInstance()
                    .reference
                    .child("userclinic")
                    .child(userId!!)


            userRef.child("token").setValue(token).addOnCompleteListener {
                if (it.isSuccessful) {
                    prefs["tokenSended"] = true
                }
            }

        }

    }

    private fun sendNotification(message: RemoteMessage?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("reservasi", 1)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT)

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(message?.data?.get("title"))
                .setContentText(message?.data?.get("message"))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 , notificationBuilder.build())
    }

    companion object {

        private const val TAG = "MyFirebaseMsgService"
    }
}