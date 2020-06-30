package com.sam.sabinomeals.services



import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.sam.sabinomeals.commons.Common
import java.util.*

class MyFCMServices : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val dataRecv = remoteMessage.data
        Common.showNotification(this, Random().nextInt(),
            dataRecv[Common.NOTI_TITLE],
            dataRecv[Common.NOTI_CONTENT],
            null)
    }
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    Common.updateToken(this,token)
    }
}