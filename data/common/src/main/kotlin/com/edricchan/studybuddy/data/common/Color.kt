package com.edricchan.studybuddy.data.common

import androidx.annotation.ColorInt
import androidx.annotation.Discouraged
import androidx.annotation.IntRange
import androidx.core.graphics.alpha
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red

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
 * The internal [value] is an ARGB colour representation; to get its components,
 * the desired [alpha], [red], [green] and [blue] properties can be used.
 */
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
}

/** Creates a [Color] using the specified colour's components. */
fun Color(
    @IntRange(from = 0, to = 255)
    alpha: Int,
    @IntRange(from = 0, to = 255)
    red: Int,
    @IntRange(from = 0, to = 255)
    green: Int,
    @IntRange(from = 0, to = 255)
    blue: Int,
): Color = Color(
    (alpha shl 24) or (red shl 16) or (green shl 8) or blue
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
    )
)
