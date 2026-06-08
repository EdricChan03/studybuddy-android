package com.edricchan.studybuddy.data.common

import androidx.annotation.ColorInt
import androidx.annotation.Discouraged
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.core.graphics.alpha
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import kotlinx.serialization.Serializable
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.random.Random

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

    /**
     * Returns the raw ARGB hexadecimal string (i.e. `AARRGGBB`) equivalent of this colour.
     *
     * To get a prefixed `#` version, use [toArgbHexString].
     */
    fun toRawArgbHexString(): String = "%08X".format(value and 0xFFFFFFFF.toInt())

    /** Returns the RGB hexadecimal string (i.e. `#RRGGBB`) equivalent of this colour. */
    fun toRgbHexString(): String =
        "#%06X".format(value and 0xFFFFFF)

    /**
     * Returns the raw RGB hexadecimal string (i.e. `RRGGBB`) equivalent of this colour.
     *
     * To get a prefixed `#` version, use [toRgbHexString].
     */
    fun toRawRgbHexString(): String = "%06X".format(value and 0xFFFFFF)

    companion object {
        //#region HSV/HSL operations
        // Code shamelessly stolen from Compose's Color.kt
        /**
         * Return a [Color] from [hue], [saturation], and [value] (HSV representation).
         *
         * @param hue The colour value in the range (0..360), where 0 is red, 120 is green, and 240
         *   is blue
         * @param saturation The amount of [hue] represented in the colour in the range (0..1), where
         *   0 has no colour and 1 is fully saturated.
         * @param alpha Alpha channel to apply to the computed colour
         * @param value The strength of the colour, where 0 is black.
         */
        fun hsv(
            hue: Float,
            saturation: Float,
            value: Float,
            alpha: Float = 1f,
        ): Color {
            require(hue in 0f..360f && saturation in 0f..1f && value in 0f..1f) {
                "HSV ($hue, $saturation, $value) must be in range (0..360, 0..1, 0..1)"
            }
            val red = hsvToRgbComponent(5, hue, saturation, value)
            val green = hsvToRgbComponent(3, hue, saturation, value)
            val blue = hsvToRgbComponent(1, hue, saturation, value)
            return Color(red = red, green = green, blue = blue, alpha = alpha)
        }

        private fun hsvToRgbComponent(n: Int, h: Float, s: Float, v: Float): Float {
            val k = (n.toFloat() + h / 60f) % 6f
            return v - (v * s * max(0f, minOf(k, 4 - k, 1f)))
        }

        /**
         * Return a [Color] from [hue], [saturation], and [lightness] (HSL representation).
         *
         * @param hue The colour value in the range (0..360), where 0 is red, 120 is green, and 240
         *   is blue
         * @param saturation The amount of [hue] represented in the color in the range (0..1), where
         *   0 has no colour and 1 is fully saturated.
         * @param lightness A range of (0..1) where 0 is black, 0.5 is fully colored, and 1 is
         *   white.
         * @param alpha Alpha channel to apply to the computed color
         */
        fun hsl(
            hue: Float,
            saturation: Float,
            lightness: Float,
            alpha: Float = 1f,
        ): Color {
            require(hue in 0f..360f && saturation in 0f..1f && lightness in 0f..1f) {
                "HSL ($hue, $saturation, $lightness) must be in range (0..360, 0..1, 0..1)"
            }
            val red = hslToRgbComponent(0, hue, saturation, lightness)
            val green = hslToRgbComponent(8, hue, saturation, lightness)
            val blue = hslToRgbComponent(4, hue, saturation, lightness)
            return Color(red = red, green = green, blue = blue, alpha = alpha)
        }

        private fun hslToRgbComponent(n: Int, h: Float, s: Float, l: Float): Float {
            val k = (n.toFloat() + h / 30f) % 12f
            val a = s * min(l, 1f - l)
            return l - a * max(-1f, minOf(k - 3, 9 - k, 1f))
        }
        //#endregion

        //#region Generate random colour
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
         * If the alpha channel being randomised is not desired, consider using [Color.random] instead.
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
         * If the alpha channel being randomised is not desired, consider using [Color.random] instead.
         */
        fun randomArgb(random: Random): Color = Color(
            red = (0..255).random(random),
            green = (0..255).random(random),
            blue = (0..255).random(random),
            alpha = (0..255).random(random),
        )
        //#endregion
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
