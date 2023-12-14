package com.edricchan.studybuddy.ui.widgets.compose.list

import androidx.compose.material3.ListItemColors
import androidx.compose.ui.graphics.Color
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.equals.shouldNotBeEqual

private val colors = ListItemColors(
    containerColor = Color.Red,
    headlineColor = Color.Yellow,
    leadingIconColor = Color.Green,
    overlineColor = Color.Blue,
    supportingTextColor = Color.Cyan,
    trailingIconColor = Color.Magenta,
    disabledHeadlineColor = Color.DarkGray,
    disabledLeadingIconColor = Color.White,
    disabledTrailingIconColor = Color.Black,
)

class DisabledListItemColorsTest : DescribeSpec({
    describe("withDisabledColors") {
        it("should get the disabled colours if enabled = false") {
            with(colors.withDisabledColors(enabled = false)) {
                headlineColor shouldBeEqual disabledHeadlineColor
                leadingIconColor shouldBeEqual disabledLeadingIconColor
                trailingIconColor shouldBeEqual disabledTrailingIconColor
            }
        }

        it("should get the enabled colours if enabled = true") {
            with(colors.withDisabledColors(enabled = true)) {
                headlineColor shouldNotBeEqual disabledHeadlineColor
                leadingIconColor shouldNotBeEqual disabledLeadingIconColor
                trailingIconColor shouldNotBeEqual disabledTrailingIconColor
            }
        }
    }
})
