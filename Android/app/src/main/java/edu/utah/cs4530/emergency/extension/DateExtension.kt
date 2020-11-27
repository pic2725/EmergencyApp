package com.telcuon.appcard.restful.extension

import java.text.SimpleDateFormat
import java.util.*

fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

fun Date(dateString: String, format: String, locale: Locale = Locale.getDefault()): Date = SimpleDateFormat(format, locale).parse(dateString)!!

fun GetCurrentDateTime(): Date {
    return Calendar.getInstance().time
}