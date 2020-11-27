package edu.utah.cs4530.emergency.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import edu.utah.cs4530.emergency.const.DatabaseConst
import edu.utah.cs4530.emergency.dao.UserDAO
import edu.utah.cs4530.emergency.extension.getLogger

/**
 * Query could be failed
 *
 * @return Query Result / User Data / Exception
 */
typealias GetUserHandler = (Boolean, UserDAO?, Exception?) -> Unit

/**
 * Query could be failed
 *
 * @return Query Result / UID / User Data / Exception
 */
typealias GetUserByPhoneNumberHandler = (Boolean, String?, UserDAO?, Exception?) -> Unit

/**
 * User Data Manage Class
 *
 * @author Kyungyoon Kim
 */
object UserRepository
{
    private val logger by getLogger()
    private val database: DatabaseReference = Firebase.database.reference

    /**
     * Get user's Firebase information
     *
     * @return Returns null when user is not log-on
     */
    fun getFirebaseUser(): FirebaseUser?
    {
        return FirebaseAuth.getInstance().currentUser
    }

    /**
     * Get user data object by uid
     */
    fun getUserOnce(uid: String, handler: GetUserHandler)
    {
        database.child(DatabaseConst.DOCUMENT_USERS).child(uid).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                handler.invoke(true, dataSnapshot.getValue(UserDAO::class.java), null)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                handler.invoke(false, null, databaseError.toException())
            }
        })
    }

    /**
     * Update or set user data by uid
     */
    fun setUser(uid: String, userDAO: UserDAO)
    {
        database.child(DatabaseConst.DOCUMENT_USERS).child(uid).setValue(userDAO)
    }

    /**
     * Search user data object by phone number.
     */
    fun searchUserByPhoneNumber(phoneNumber: String, handler: GetUserByPhoneNumberHandler)
    {
        val query = database.child(DatabaseConst.DOCUMENT_USERS).orderByChild(DatabaseConst.ITEM_PHONE_NUMBER).equalTo(phoneNumber)

        query.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.children.first()
                val uid = data.key
                val userDAO = data.getValue(UserDAO::class.java)

                handler.invoke(true, uid, userDAO, null)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                handler.invoke(false, null, null, databaseError.toException())
            }
        })
    }

    /*
    UserRepository.setUser("test", UserDAO(
    "Test",
    "+112312341234",
    "testIamge",
    "testFcmToken",
    ArrayList(),
    ArrayList()
    ))

    UserRepository.setUser("test1", UserDAO(
    "Test1",
    "+112312341235",
    null,
    null,
    ArrayList(),
    ArrayList()
    ))

    UserRepository.searchUserByPhoneNumber("+112312341235") { result: Boolean, uid: String?, userDAO: UserDAO?, exception: Exception? ->
    logger.debug("""
    BOOL : $result
    UID: $uid
    UserDAO: $userDAO
    Exception: $exception
    """.trimIndent())
    }

    UserRepository.getUserOnce("test") { result: Boolean, userDAO: UserDAO?, exception: Exception? ->
    logger.debug("""
    BOOL : $result
    UserDAO: $userDAO
    Exception: $exception
    """.trimIndent())
    }
     */
}