package com.edricchan.studybuddy.interfaces

import android.graphics.Color
import androidx.annotation.ColorRes
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

/**
 * A task project
 * @property color The color/colour of this project as a hexadecimal value
 * @property name The name of this project
 */
@IgnoreExtraProperties
data class TodoProject(
    @DocumentId override val id: String = "",
    var color: String? = null,
    var name: String? = null,
    @ServerTimestamp override val createdAt: Timestamp? = null,
    @ServerTimestamp override val lastModified: Timestamp? = null
) : HasId, HasTimestampMetadata {
    private constructor(builder: Builder) : this(
        color = builder.color,
        name = builder.name,
        createdAt = builder.createdAt,
        lastModified = builder.lastModified
    )

    companion object {
        /**
         * Creates a [TodoItem] using a [Builder] (with support for inlined setting of variables)
         * @return The created [TodoItem]
         */
        inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    class Builder {
        /**
         * The project's colour/color as a hexadecimal color
         */
        var color: String? = null

        /**
         * The project's ID
         */
        @Deprecated("This is kept for compatibility purposes and should not be used for future code.")
        var id: String? = null

        /**
         * The project's name
         */
        var name: String? = null

        /**
         * The timestamp the project was created at
         *
         * If left null, this field will be replaced with a server-generated timestamp
         * @see ServerTimestamp
         */
        var createdAt: Timestamp? = null

        /**
         * The timestamp the project was last modified at
         *
         * If left null, this field will be replaced with a server-generated timestamp
         * @see ServerTimestamp
         */
        var lastModified: Timestamp? = null

        /**
         * Checks if an RGB code is valid
         *
         * @param code The code to check
         * @return True if it is valid, false otherwise
         */
        private fun checkValidRGBCode(code: Int): Boolean {
            return code in 0..255
        }

        /**
         * Converts a color from a resource to a hexdecimal color
         *
         * @param color The color to convert
         * @return The color in hexadecimal form
         */
        private fun convertColorToHex(@ColorRes color: Int): String {
            return String.format("#%06X", 0xFFFFFF and color)
        }

        /**
         * Converts a RGB color to a hexdecimal color
         *
         * @param r The red value
         * @param g The green value
         * @param b The blue value
         * @return The color in hexadecimal form
         */
        private fun convertRGBtoHex(r: Int, g: Int, b: Int): String {
            return String.format("#%02x%02x%02x", r, g, b)
        }

        /**
         * Sets the color of this project
         *
         * @param color A hexadecimal color
         * @return The builder object to allow for chaining of methods
         */
        fun setColor(color: String): Builder {
            try {
                Color.parseColor(color)
                // The color is valid
                this.color = color
            } catch (iae: IllegalArgumentException) {
                // This color string is not valid
                throw IllegalArgumentException("Please supply a valid hexadecimal color!")
            }

            return this
        }

        /**
         * Sets the color of this project
         *
         * @param r The red value
         * @param g The green value
         * @param b The blue value
         * @return The builder object to allow for chaining of methods
         */
        fun setColor(r: Int, g: Int, b: Int): Builder {
            if (checkValidRGBCode(r)) {
                if (checkValidRGBCode(g)) {
                    if (checkValidRGBCode(b)) {
                        this.color = this.convertRGBtoHex(r, g, b)
                    } else {
                        throw IllegalArgumentException("Please supply a valid RGB blue code!")
                    }
                } else {
                    throw IllegalArgumentException("Please supply a valid RGB green code!")
                }
            } else {
                throw IllegalArgumentException("Please supply a valid RGB red code!")
            }
            return this
        }

        /**
         * Sets the color of this project
         *
         * @param color A color resource
         * @return The builder object to allow for chaining of methods
         */
        fun setColor(@ColorRes color: Int): Builder {
            this.color = this.convertColorToHex(color)
            return this
        }

        /**
         * Creates the [TodoProject]
         * @return The created [TodoProject]
         */
        fun build() = TodoProject(this)
    }
}
