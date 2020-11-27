package edu.utah.cs4530.emergency.ui.editmessage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import edu.utah.cs4530.emergency.const.DatabaseConst
import edu.utah.cs4530.emergency.extension.getLogger

class EditMessageViewModel : ViewModel() {
    private val logger by getLogger()

    private val uid = FirebaseAuth.getInstance().currentUser?.uid ?: throw Exception("User is not logged-in")
    private val database = Firebase.database.reference
        .child(DatabaseConst.DOCUMENT_USERS)
        .child(uid)
        .child(DatabaseConst.EMERGENCY_MESSAGE)

    private val _emergencyMessage = MutableLiveData<String>()
    val emergencyMessage: LiveData<String> = _emergencyMessage

    private val emergencyMessageTextEventListener = object: ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            _emergencyMessage.value = dataSnapshot.getValue(String::class.java) ?: ""
        }

        override fun onCancelled(error: DatabaseError) {
            logger.error("Failed to get emergency message", error.toException())
            throw error.toException()
        }
    }

    init {
        database.addValueEventListener(emergencyMessageTextEventListener)
    }

    fun saveEmergencyMessage(newMessage: String) {
        database.setValue(newMessage)
    }

}
