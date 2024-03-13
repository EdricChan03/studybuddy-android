package com.edricchan.studybuddy.ui.widgets.compose

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed

internal fun SemanticsNodeInteraction.assertExistsAndIsDisplayed() {
    assertExists()
    assertIsDisplayed()
}
