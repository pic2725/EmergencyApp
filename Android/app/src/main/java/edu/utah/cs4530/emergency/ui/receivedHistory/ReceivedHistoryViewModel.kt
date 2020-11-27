package edu.utah.cs4530.emergency.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import edu.utah.cs4530.emergency.const.DatabaseConst
import edu.utah.cs4530.emergency.dao.AlertReceivedHistoryDAO
import edu.utah.cs4530.emergency.extension.getLogger
import edu.utah.cs4530.emergency.repository.UserRepository

class ReceivedHistoryViewModel: ViewModel()
{
    private val logger by getLogger()

    private val uid = UserRepository.getFirebaseUser()?.uid ?: throw Exception("User is not logged-in")
    private val database = Firebase.database.reference
        .child(DatabaseConst.DOCUMENT_USERS)
        .child(uid)
        .child(DatabaseConst.ITEM_ALERT_RECEIVED_HISTORIES)

    private val _histories = MutableLiveData<ArrayList<AlertReceivedHistoryDAO>>(ArrayList())
    val histories: LiveData<ArrayList<AlertReceivedHistoryDAO>>
        get() = _histories

    private val historiesValueEventListener = object: ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            logger.debug("Data updated [$dataSnapshot]")

            //Map object to ArrayList of ContactDAO.
            _histories.value = ArrayList(dataSnapshot.children.map {it.getValue(AlertReceivedHistoryDAO::class.java) ?: throw Exception("Fail to convert object to AlertReceivedHistoryDAO")}).apply{ this.sortByDescending { it.time }}
        }

        override fun onCancelled(error: DatabaseError) {
            logger.error("Fail to get user data object.", error.toException())
            throw error.toException()
        }
    }

    init
    {
        database.addValueEventListener(historiesValueEventListener)
    }

    override fun onCleared() {
        database.removeEventListener(historiesValueEventListener)
    }
}