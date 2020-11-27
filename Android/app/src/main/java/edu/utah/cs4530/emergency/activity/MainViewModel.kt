package edu.utah.cs4530.emergency.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import edu.utah.cs4530.emergency.const.DatabaseConst
import edu.utah.cs4530.emergency.dao.UserDAO
import edu.utah.cs4530.emergency.extension.getLogger
import edu.utah.cs4530.emergency.repository.UserRepository

class MainViewModel : ViewModel() {
    private val logger by getLogger()

    private val uid = UserRepository.getFirebaseUser()?.uid ?: throw Exception("User is not logged-in")
    private val database = Firebase.database.reference
        .child(DatabaseConst.DOCUMENT_USERS)
        .child(uid)

    private val _userDao = MutableLiveData<UserDAO>()
    val userDao: LiveData<UserDAO>
        get() = _userDao

    private val contactListValueEventListener = object: ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            logger.debug("Data updated [$dataSnapshot]")

            //Map object to ArrayList of ContactDAO.
            _userDao.value = dataSnapshot.getValue(UserDAO::class.java) ?: throw Exception("Fail to convert object to UserDAO")
        }

        override fun onCancelled(error: DatabaseError) {
            logger.error("Fail to get user data object.", error.toException())
            throw error.toException()
        }
    }

    init
    {
        database.addValueEventListener(contactListValueEventListener)
    }

    override fun onCleared() {
        database.removeEventListener(contactListValueEventListener)
    }


}