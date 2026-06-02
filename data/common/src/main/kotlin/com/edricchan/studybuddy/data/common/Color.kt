package com.edricchan.studybuddy.data.common

import androidx.annotation.ColorInt
import androidx.annotation.Discouraged
import androidx.annotation.IntRange
import androidx.core.graphics.alpha
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import kotlinx.serialization.Serializable

private fun getWebHexFormat(
    withSignPrefix: Boolean = true,
    minLength: Int
) = HexFormat {
    upperCase = true
    number {
        prefix = if (withSignPrefix) "#" else ""
        this.minLength = minLength
    }
}

/**
 * Value class for a colour integer.
 *
 * The internal [value] is an ARGB colour representation - to get its components,
 * the desired [alpha], [red], [green] and [blue] properties can be used.
 */
@Serializable
@JvmInline
value class Color(
    @field:ColorInt
    val value: Int
) {
    /**
     * Gets the alpha component of this colour integer.
     * @see androidx.core.graphics.alpha
     */
    val alpha: Int get() = value.alpha

    /**
     * Gets the red component of this colour integer.
     * @see androidx.core.graphics.red
     */
    val red: Int get() = value.red

    /**
     * Gets the green component of this colour integer.
     * @see androidx.core.graphics.green
     */
    val green: Int get() = value.green

    /**
     * Gets the blue component of this colour integer.
     * @see androidx.core.graphics.blue
     */
    val blue: Int get() = value.blue

    /**
     * Gets the alpha component of this colour integer, using destructuring syntax:
     * ```
     * val (alpha, red, green, blue) = Color(0xFFFFFFFF)
     * ```
     * @see alpha
     * @see androidx.core.graphics.alpha
     */
    operator fun component1(): Int = alpha

    /**
     * Gets the red component of this colour integer, using destructuring syntax:
     * ```
     * val (alpha, red, green, blue) = Color(0xFFFFFFFF)
     * ```
     * @see red
     * @see androidx.core.graphics.red
     */
    operator fun component2(): Int = red

    /**
     * Gets the green component of this colour integer, using destructuring syntax:
     * ```
     * val (alpha, red, green, blue) = Color(0xFFFFFFFF)
     * ```
     * @see green
     * @see androidx.core.graphics.green
     */
    operator fun component3(): Int = green

    /**
     * Gets the blue component of this colour integer, using destructuring syntax:
     * ```
     * val (alpha, red, green, blue) = Color(0xFFFFFFFF)
     * ```
     * @see blue
     * @see androidx.core.graphics.blue
     */
    operator fun component4(): Int = blue

    /** Returns the ARGB hexadecimal string (i.e. `#AARRGGBB`) equivalent of this colour. */
    fun toArgbHexString(): String =
        "#%08X".format(value and 0xFFFFFFFF.toInt())

    /** Returns the RGB hexadecimal string (i.e. `#RRGGBB`) equivalent of this colour. */
    fun toRgbHexString(): String =
        "#%06X".format(value and 0xFFFFFF)

    companion object {
        /** Generates a random colour with the red, green and blue channels set to random values. */
        fun random(): Color = Color(
            red = (0..255).random(),
            green = (0..255).random(),
            blue = (0..255).random(),
        )

        /**
         * Generates a random colour with the red, green and blue channels set to random values as
         * seeded by the `random` argument.
         */
        fun random(random: Random): Color = Color(
            red = (0..255).random(random),
            green = (0..255).random(random),
            blue = (0..255).random(random),
        )

        /**
         * Generates a random colour with the alpha, red, green and blue channels set to random
         * values.
         *
         * If the alpha channel being randomised is not desired, consider using [random] instead.
         */
        fun randomArgb(): Color = Color(
            red = (0..255).random(),
            green = (0..255).random(),
            blue = (0..255).random(),
            alpha = (0..255).random(),
        )

        /**
         * Generates a random colour with the alpha, red, green and blue channels set to random
         * values as seeded by the `random` argument.
         *
         * If the alpha channel being randomised is not desired, consider using [random] instead.
         */
        fun randomArgb(random: Random): Color = Color(
            red = (0..255).random(random),
            green = (0..255).random(random),
            blue = (0..255).random(random),
            alpha = (0..255).random(random),
        )
    }
}

/** Creates a [Color] using the specified colour's components. */
fun Color(
    @IntRange(from = 0, to = 255)
    red: Int,
    @IntRange(from = 0, to = 255)
    green: Int,
    @IntRange(from = 0, to = 255)
    blue: Int,
    @IntRange(from = 0, to = 255)
    alpha: Int = 255
): Color = Color(
    (alpha shl 24) or (red shl 16) or (green shl 8) or blue
)

/** Creates a [Color] using the specified colour's components. */
fun Color(
    @FloatRange(from = 0.0, to = 1.0)
    red: Float,
    @FloatRange(from = 0.0, to = 1.0)
    green: Float,
    @FloatRange(from = 0.0, to = 1.0)
    blue: Float,
    @FloatRange(from = 0.0, to = 1.0)
    alpha: Float = 1f
): Color = Color(
    red = red.coerceIn(0.0f, 1.0f).roundToInt(),
    green = green.coerceIn(0.0f, 1.0f).roundToInt(),
    blue = blue.coerceIn(0.0f, 1.0f).roundToInt(),
    alpha = alpha.coerceIn(0.0f, 1.0f).roundToInt(),
)

/** Creates a [Color] using the specified 6-digit hexadecimal string (in `#RRGGBB` format). */
@Discouraged(
    "Prefer using a colour integer instead of a hexadecimal colour, which " +
        "can differ across platforms (e.g. web uses #RRGGBBAA while Android uses #AARRGGBB)"
)
fun Color(
    hexString: String
): Color = Color(
    hexString.hexToInt(
        getWebHexFormat(
            minLength = 6
        )
    ) or 0xff000000.toInt()
)
