package edu.utah.cs4530.emergency.activity

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.tcodevice.card.tada.consts.NotificationConst
import edu.utah.cs4530.emergency.R
import edu.utah.cs4530.emergency.util.EzNotification

class SplashActivity: AppCompatActivity(), PermissionListener
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            EzNotification.createChannel(
                applicationContext,
                NotificationConst.EMERGENCY,
                NotificationConst.EMERGENCY_NAME, EzNotification.IMPORTANCE_HIGH
            )
        }

        TedPermission.with(applicationContext)
            .setPermissionListener(this)
            .setDeniedMessage("If you reject permission, we cannot track your current location.")
            .setPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_PHONE_STATE
            )
            .check()
    }

    override fun onPermissionGranted() {
        //Check user is logged in
        if(FirebaseAuth.getInstance().currentUser == null)
        {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        else
        {
            startActivity(Intent(this, MainActivity::class.java))
            //startActivity(Intent(this, TutorialActivity::class.java))
        }
    }

    override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
        finish()
    }
}