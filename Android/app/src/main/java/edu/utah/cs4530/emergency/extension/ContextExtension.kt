package edu.utah.cs4530.emergency.extension

import android.content.Context
import androidx.appcompat.app.AlertDialog

fun Context.showMessageBox(title: String, message: String, okHandler: (() -> Unit)? = null)
{
    val builder = AlertDialog.Builder(this)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setPositiveButton("OK") {_, _ ->
        okHandler?.invoke()
    }
    builder.show()
}