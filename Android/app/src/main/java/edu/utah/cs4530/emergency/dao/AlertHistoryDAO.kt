package edu.utah.cs4530.emergency.dao

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable
import java.util.*

/**
 * AlertHistoryDAO
 *
 * This data object has alert historical data.
 */
@IgnoreExtraProperties
data class AlertHistoryDAO(
    val time: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val contactedUserPhoneNumber: HashMap<String, AlertHistoryUserDAO> = HashMap()
): Serializable

