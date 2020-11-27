package edu.utah.cs4530.emergency.repository

import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import edu.utah.cs4530.emergency.activity.TutorialActivity
import edu.utah.cs4530.emergency.dao.UserDAO
import edu.utah.cs4530.emergency.util.DeviceInfo
import edu.utah.cs4530.emergency.util.WSharedPref
import kotlinx.android.synthetic.main.activity_login.*

object DeviceRepository
{
    private const val KEY_FCM_TOKEN = "FCM_TOKEN"
    var fcmToken: String?
        get() {
            return WSharedPref.getString(KEY_FCM_TOKEN, null)
        }
        set(newValue) {
            FirebaseAuth.getInstance().uid?.let {uid ->
                UserRepository.getUserOnce(uid) { result: Boolean, userDAO: UserDAO?, exception: Exception? ->
                    if(result && userDAO != null)
                    {
                        UserRepository.setUser(uid, userDAO.apply {
                            fcmToken = newValue
                        })
                    }
                }
            }

            WSharedPref.setString(KEY_FCM_TOKEN, newValue)
        }


}