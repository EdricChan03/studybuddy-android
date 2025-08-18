package com.edricchan.studybuddy.features.tasks.data.model

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.Keep
import com.edricchan.studybuddy.data.common.HasId
import com.edricchan.studybuddy.data.common.HasTimestampMetadata
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

/**
 * A task project.
 * @property color The colour of this project as a hexadecimal value.
 * @property name The name of this project.
 */
@IgnoreExtraProperties
@Keep
data class TodoProject(
    @DocumentId override val id: String = "",
    val color: String? = null,
    val name: String? = null,
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
         * Converts a color from its integer representation to a hexadecimal color
         *
         * @param color The color to convert
         * @return The color in hexadecimal form
         */
        private fun convertColorToHex(@ColorInt color: Int): String {
            return String.format("#%06X", 0xFFFFFF and color)
        }

        /**
         * Converts a RGB color to a hexadecimal color
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
            require(checkValidRGBCode(r)) { "Please supply a valid RGB red code!" }
            require(checkValidRGBCode(g)) { "Please supply a valid RGB green code!" }
            require(checkValidRGBCode(b)) { "Please supply a valid RGB blue code!" }

            this.color = this.convertRGBtoHex(r, g, b)
            return this
        }

        /**
         * Sets the colour of this project
         *
         * @param rgb The red, green and blue values as a [Triple]
         * @return The builder object to allow for chaining of methods
         */
        fun setColor(rgb: Triple<Int, Int, Int>) = setColor(rgb.first, rgb.second, rgb.third)

        /**
         * Sets the colour of this project.
         *
         * @param color A color integer.
         * @return The builder object to allow for chaining of methods
         */
        fun setColor(@ColorInt color: Int): Builder {
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
