package edu.utah.cs4530.emergency.dao

import android.net.Uri
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ContactDAO (
    var name: String? = null,
    var phoneNumber: String? = null,
    var photoUri: String? = null
)