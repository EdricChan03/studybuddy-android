package com.edricchan.studybuddy.features.tasks.data.model

import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import androidx.annotation.Keep
import com.edricchan.studybuddy.data.common.Color
import com.edricchan.studybuddy.data.common.DtoModel
import com.edricchan.studybuddy.data.common.HasId
import com.edricchan.studybuddy.data.common.HasTimestampMetadata
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

/**
 * A task project.
 * @property color The colour of this project as a web-safe hexadecimal string. This should
 * be a 6-digit hexadecimal colour, in the format `#RRGGBB`.
 * @property colorInt Colour to be used when this project data is displayed in
 * the user interface, which may be themed accordingly.
 * @property name The name of this project.
 */
@IgnoreExtraProperties
@Keep
data class TodoProject(
    @DocumentId override val id: String = "",
    @Deprecated(
        "Use colorValue where preferable, which stores the colour data as a " +
            "more typical colour integer rather than as a hexadecimal string which can be " +
            "represented differently across platforms (e.g. Android uses #AARRGGBB, web " +
            "uses #RRGGBBAA)"
    )
    val color: String? = null,
    @field:ColorInt
    val colorInt: Int? = null,
    val name: String? = null,
    @ServerTimestamp override val createdAt: Timestamp? = null,
    @ServerTimestamp override val lastModified: Timestamp? = null
) : DtoModel, HasId, HasTimestampMetadata {
    private constructor(builder: Builder) : this(
        color = builder.colorValue?.toRgbHexString(),
        colorInt = builder.colorValue?.value,
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
         * Desired colour to be used when this project data is displayed in the user interface,
         * which may be themed accordingly for contrast.
         */
        var colorValue: Color? = null

        /** The project's colour as a hexadecimal colour in `#RRGGBB` form. */
        @Deprecated(
            "Use colorValue where preferable, which stores the colour data as a " +
                "more typical colour integer rather than as a hexadecimal string which can be " +
                "represented differently across platforms (e.g. Android uses #AARRGGBB, web " +
                "uses #RRGGBBAA)"
        )
        var color: String?
            get() = colorValue?.toRgbHexString()
            set(value) {
                colorValue = value?.let { Color(hexString = it) }
            }

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
         * Sets the colour of this project
         *
         * @param color A hexadecimal colour
         * @return The builder object to allow for chaining of methods
         */
        @Deprecated(
            "Use colorValue where preferable, which stores the colour data as a " +
                "more typical colour integer rather than as a hexadecimal string which can be " +
                "represented differently across platforms (e.g. Android uses #AARRGGBB, web " +
                "uses #RRGGBBAA)"
        )
        fun setColor(color: String): Builder {
            colorValue = Color(hexString = color)
            return this
        }

        /**
         * Sets the colour of this project
         *
         * @param r The red component - this value must be in the range `0..255`.
         * @param g The green component - this value must be in the range `0..255`.
         * @param b The blue component - this value must be in the range `0..255`.
         * @return The builder object to allow for chaining of methods
         */
        fun setColor(
            @IntRange(from = 0, to = 255)
            r: Int,
            @IntRange(from = 0, to = 255)
            g: Int,
            @IntRange(from = 0, to = 255)
            b: Int
        ): Builder {
            require(checkValidRGBCode(r)) { "Please supply a valid RGB red code!" }
            require(checkValidRGBCode(g)) { "Please supply a valid RGB green code!" }
            require(checkValidRGBCode(b)) { "Please supply a valid RGB blue code!" }

            colorValue = Color(
                alpha = 255,
                red = r,
                green = g,
                blue = b
            )
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
            colorValue = Color(color)
            return this
        }

        /**
         * Creates the [TodoProject]
         * @return The created [TodoProject]
         */
        fun build() = TodoProject(this)
    }
}
