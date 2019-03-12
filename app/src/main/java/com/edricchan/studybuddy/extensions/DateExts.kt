package com.edricchan.studybuddy.extensions

import com.google.firebase.Timestamp
import java.util.*

/**
 * Converts a [Date] to its [Timestamp] equivalent
 * @return A [Timestamp]
 */
fun Date?.toTimestamp() = Timestamp(this)
