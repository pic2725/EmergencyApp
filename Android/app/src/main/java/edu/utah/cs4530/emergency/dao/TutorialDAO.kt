package edu.utah.cs4530.emergency.dao

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class TutorialDAO (
    val title: String,
    val description: String,
    val screen: Int
)