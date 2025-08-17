package com.edricchan.studybuddy.data.common

import androidx.annotation.ColorInt
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

    /**
     * Gets the ARGB hexadecimal string (i.e. `#AARRGGBB`) equivalent of this colour.
     * @param withSignPrefix Whether the `"#"` prefix symbol should be included.
     */
    fun toArgbHexString(withSignPrefix: Boolean = true): String =
        value.toHexString(format = getWebHexFormat(withSignPrefix = withSignPrefix, minLength = 8))

    /**
     * Gets the RGB hexadecimal string (i.e. `#RRGGBB`) equivalent of this colour.
     * @param withSignPrefix Whether the `"#"` prefix symbol should be included.
     */
    fun toRgbHexString(withSignPrefix: Boolean = true): String =
        (value and 0xFFFFFF).toHexString(
            format = getWebHexFormat(
                withSignPrefix = withSignPrefix,
                minLength = 6
            )
        )
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

/** Creates a [Color] using the specified hexadecimal string. */
fun Color(
    hexString: String
): Color = Color(
    hexString.hexToInt(
        getWebHexFormat(
            minLength = 6
        )
    )
)
