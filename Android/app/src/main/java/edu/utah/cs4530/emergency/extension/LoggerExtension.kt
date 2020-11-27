package edu.utah.cs4530.emergency.extension

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Extension Method
 *
 * Example : private val getLogger by getLogger()
 */
fun <R : Any> R.getLogger(): Lazy<Logger> {
    return lazy { LoggerFactory.getLogger(this.javaClass) }
}
