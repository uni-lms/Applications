package ru.aip.intern.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel


@AndroidEntryPoint
class MessagingService : FirebaseMessagingService() {

    //    @Inject
//    lateinit var storage: DataStoreRepository
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onNewToken(token: String) {
        Log.d("FCM", "Refreshed token: $token")

        // TODO: add way to
        // 1. get token
        // if it's presented, then update FCM token for specific user TODO 2 for backend: add endpoint for this
        // if not, do nothing
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}