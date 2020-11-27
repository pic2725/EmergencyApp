package edu.utah.cs4530.emergency.util

import android.content.Context
import android.net.ConnectivityManager
import edu.utah.cs4530.emergency.EmergencyApplication
import edu.utah.cs4530.emergency.extension.getLogger

/**
 *
 *
 * @author Kyungyoon Kim
 * @since 2018.1.1
 * @version 1.1.0
 */
object NetworkUtil {
    private val logger by getLogger()

    fun isNetworkAvailable(): Boolean {
        return try {
            val connectivityManager =
                EmergencyApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            activeNetworkInfo != null && activeNetworkInfo.isConnected
        } catch (e: Exception) {
            logger.error(e.toString())
            true
        }
    }

    fun isUsingWifi(): Boolean {
        return try {
            val connMgr =
                EmergencyApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            wifi.isConnectedOrConnecting
        } catch (e: Exception) {
            logger.error(e.toString())
            false
        }
    }

    fun isUsingCellular(): Boolean {
        return try {
            val connection_manager =
                EmergencyApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connection_manager.networkPreference = ConnectivityManager.TYPE_MOBILE
            val cmClass =
                Class.forName(connection_manager.javaClass.name)
            val method = cmClass.getDeclaredMethod("getMobileDataEnabled")
            method.isAccessible = true
            method.invoke(connection_manager) as Boolean
        } catch (e: Exception) {
            logger.error(e.toString())
            false
        }
    }
}