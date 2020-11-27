package edu.utah.cs4530.emergency.dao

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.IgnoreExtraProperties
import edu.utah.cs4530.emergency.repository.DeviceRepository
import edu.utah.cs4530.emergency.util.DeviceInfo

@IgnoreExtraProperties
data class UserDAO (
    var name: String? = FirebaseAuth.getInstance().currentUser?.displayName,
    var phoneNumber: String? = DeviceInfo.phoneNumber,
    var imageUrl: String? = FirebaseAuth.getInstance().currentUser?.photoUrl.toString(),
    var fcmToken: String? = DeviceRepository.fcmToken,
    var emergencyMessage: String = "Help me, I am in emergency situation.",
    var contactList: ArrayList<ContactDAO> = ArrayList(),
    var alertHistories: HashMap<String, AlertHistoryDAO> = HashMap()
)