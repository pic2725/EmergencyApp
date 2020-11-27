package edu.utah.cs4530.emergency.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.telephony.TelephonyManager
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.gun0912.tedpermission.TedPermission
import edu.utah.cs4530.emergency.EmergencyApplication
import edu.utah.cs4530.emergency.extension.getLogger
import java.util.*


object DeviceInfo
{
    private val logger by getLogger()

    val phoneNumber: String?
        @SuppressLint("MissingPermission")
        get() {
            val context = EmergencyApplication.context
            val tMgr = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

            if(!TedPermission.isGranted(context, Manifest.permission.READ_PHONE_STATE))
                return null
            else
            {
                val phoneNumberUtil = PhoneNumberUtil.getInstance()
                try
                {
                    val phoneNumber = phoneNumberUtil.parse(tMgr.line1Number, Locale.getDefault().country)
                    return phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164)
                }
                catch (e: Exception)
                {
                    logger.error("There was an error when reading phone number", e)
                    return null
                }
            }
        }
}