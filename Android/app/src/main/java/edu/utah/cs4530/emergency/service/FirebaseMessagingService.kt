package edu.utah.cs4530.emergency.service

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tcodevice.card.tada.consts.NotificationConst
import edu.utah.cs4530.emergency.R
import edu.utah.cs4530.emergency.activity.MainActivity
import edu.utah.cs4530.emergency.dao.AlertHistoryUserDAO
import edu.utah.cs4530.emergency.dao.AlertReceivedHistoryDAO
import edu.utah.cs4530.emergency.extension.getLogger
import edu.utah.cs4530.emergency.repository.DeviceRepository
import edu.utah.cs4530.emergency.util.EzNotification


class FirebaseMessagingService : FirebaseMessagingService() {

    private val logger by getLogger()

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

       logger.info("From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        remoteMessage.data.isNotEmpty().let {
            logger.debug("Message data payload: " + remoteMessage.data)
            val time = remoteMessage.data["time"]
            val latitude = remoteMessage.data["latitude"]
            val longitude = remoteMessage.data["longitude"]
            val emergencyMessage = remoteMessage.data["emergencyMessage"]
            val name = remoteMessage.data["name"]
            val phoneNumber = remoteMessage.data["phoneNumber"]
            val imageUrl = remoteMessage.data["imageUrl"]
            val uid = remoteMessage.data["uid"]

            val alertReceivedHistoryDAO = AlertReceivedHistoryDAO(
                time = time ?: "",
                latitude = latitude?.toDouble() ?: 0.0,
                longitude = longitude?.toDouble() ?: 0.0,
                contactedUserPhoneNumber = AlertHistoryUserDAO(
                    name = name ?: "",
                    phoneNumber = phoneNumber ?: "",
                    imageUrl = imageUrl,
                    uid = uid ?: "",
                    method = 0
                )
            )

            val intent = Intent(applicationContext, MainActivity::class.java).apply {
                action = Intent.ACTION_MAIN
                addCategory(Intent.CATEGORY_LAUNCHER)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                //putExtra(MainActivity.BUNDLE_ALERT_RECEIVED_HISTORY_DAO, alertReceivedHistoryDAO)
                putExtras(MainActivity.makeBundle(alertReceivedHistoryDAO))
            }

            val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            EzNotification(applicationContext, NotificationConst.EMERGENCY).apply {
                setIcon(R.drawable.ic_announcement_black_24dp)
                setTitle("!!SOS!! - $name")
                setMessage(emergencyMessage ?: "")
                setSubMessage("Tap to view detail about alert.")
                setVisibility(Notification.VISIBILITY_PUBLIC)
                setIntent(pendingIntent)
                setNotiToUser(true)
                setWakeUp()
            }.show()
        }
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        logger.info("Refreshed token: $token")
        DeviceRepository.fcmToken = token
    }

}