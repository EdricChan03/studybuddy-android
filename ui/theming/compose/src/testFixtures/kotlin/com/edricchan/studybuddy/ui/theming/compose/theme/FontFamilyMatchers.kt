package com.edricchan.studybuddy.ui.theming.compose.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.reflection.compose
import io.kotest.matchers.should
import io.kotest.matchers.shouldNot

/** Creates a [Matcher] for the given [family]. */
fun useFontFamily(family: FontFamily) = Matcher<TextStyle> { value ->
    MatcherResult(
        value.fontFamily == family,
        { "text-style uses font family ${value.fontFamily} but $family was expected" },
        { "text-style should not use font family $family but ${value.fontFamily} was found" },
    )
}

/** Asserts that the receiver [TextStyle] is using the specified [family]. */
infix fun TextStyle.shouldUseFontFamily(family: FontFamily) = apply {
    this should useFontFamily(family)
}

/** Asserts that the receiver [TextStyle] is not using the specified [family]. */
fun TextStyle.shouldNotUseFontFamily(family: FontFamily) = apply {
    this shouldNot useFontFamily(family)
}

/** Creates a [Matcher] for a [Typography] with the given [family] for each [TextStyle]. */
fun typographyMatcher(family: FontFamily) = Matcher.compose(
    useFontFamily(family) to Typography::displayLarge,
    useFontFamily(family) to Typography::displayMedium,
    useFontFamily(family) to Typography::displaySmall,
    useFontFamily(family) to Typography::headlineLarge,
    useFontFamily(family) to Typography::headlineMedium,
    useFontFamily(family) to Typography::headlineSmall,
    useFontFamily(family) to Typography::titleLarge,
    useFontFamily(family) to Typography::titleMedium,
    useFontFamily(family) to Typography::titleSmall,
    useFontFamily(family) to Typography::bodyLarge,
    useFontFamily(family) to Typography::bodyMedium,
    useFontFamily(family) to Typography::bodySmall,
    useFontFamily(family) to Typography::labelLarge,
    useFontFamily(family) to Typography::labelMedium,
    useFontFamily(family) to Typography::labelSmall,
)

/**
 * Asserts that the receiver [Typography] is using the specified [family]
 * for all of its [TextStyle]s.
 */
infix fun Typography.shouldUseFontFamily(family: FontFamily) = apply {
    this should typographyMatcher(family)
}

/**
 * Asserts that the receiver [Typography] is not using the specified [family]
 * for all of its [TextStyle]s.
 */
infix fun Typography.shouldNotUseFontFamily(family: FontFamily) = apply {
    this shouldNot typographyMatcher(family)
}
