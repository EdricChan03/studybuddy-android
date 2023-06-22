package com.edricchan.studybuddy.ui.theming.compose.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.reflection.compose
import io.kotest.matchers.should
import io.kotest.matchers.shouldNot

class TypographyUnitTest : FunSpec({
    test("Baloo font is applied to all text-styles") {
        StudyBuddyTypography shouldUseFontFamily baloo2Family
    }
})

// TODO: Move to test-fixtures once AGP supports Kotlin source sets for test-fixtures - see
//  - https://issuetracker.google.com/issues/139438142
//  - https://issuetracker.google.com/issues/259523353
private fun useFontFamily(family: FontFamily) = Matcher<TextStyle> { value ->
    MatcherResult(
        value.fontFamily == family,
        { "text-style uses font family ${value.fontFamily} but $family was expected" },
        { "text-style should not use font family $family but ${value.fontFamily} was found" },
    )
}

private infix fun TextStyle.shouldUseFontFamily(family: FontFamily) = apply {
    this should useFontFamily(family)
}

private fun TextStyle.shouldNotUseFontFamily(family: FontFamily) = apply {
    this shouldNot useFontFamily(family)
}

private fun typographyMatcher(family: FontFamily) = Matcher.compose(
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

private infix fun Typography.shouldUseFontFamily(family: FontFamily) = apply {
    this should typographyMatcher(family)
}

private infix fun Typography.shouldNotUseFontFamily(family: FontFamily) = apply {
    this shouldNot typographyMatcher(family)
}
