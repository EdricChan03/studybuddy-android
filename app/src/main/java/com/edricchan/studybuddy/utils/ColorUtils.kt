package com.edricchan.studybuddy.utils

import android.graphics.Color
import java.util.*

/**
 * Utility class containing color-related functionality.
 */
class ColorUtils() {
    /**
     * Creates a random color.
     */
    fun createRandomColor(): Int {
        val random = Random()
        return Color.argb(
            255, random.nextInt(256), random.nextInt(256),
            random.nextInt(256)
        )
    }

    companion object {
        /**
         * Creates a new instance of the [ColorUtils] class.
         */
        fun getInstance() = ColorUtils()
    }
}