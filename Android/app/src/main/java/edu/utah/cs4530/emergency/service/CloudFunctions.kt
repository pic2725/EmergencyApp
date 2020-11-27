package edu.utah.cs4530.emergency.service

import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.HttpsCallableResult

object CloudFunctions
{
    fun sendEmergencyMessage(latitude: Double, longitude: Double): Task<HttpsCallableResult>
    {
        val data = hashMapOf(
            "latitude" to latitude,
            "longitude" to longitude
        )

        return FirebaseFunctions.getInstance()
            .getHttpsCallable("sendEmergencyMessage")
            .call(data)
    }
}