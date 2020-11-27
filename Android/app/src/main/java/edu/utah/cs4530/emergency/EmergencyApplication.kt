package edu.utah.cs4530.emergency

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.google.firebase.FirebaseApp
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class EmergencyApplication: MultiDexApplication()
{
    override fun onCreate() {
        super.onCreate()

        //Use vector image
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        //Firebase Database Settings
        Firebase.database.setPersistenceEnabled(true)

        //Firebase initialize
        FirebaseApp.initializeApp(this)

        //Set global context
        context = applicationContext
    }

    companion object
    {
        lateinit var context: Context
    }
}