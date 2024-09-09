package com.edricchan.studybuddy.ui.preference.compose

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import kotlin.test.Test

private const val testTag = "DividerTag"

class PreferenceDividerTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun dividerTest() {
        composeTestRule.apply {
            setContent {
                PreferenceDivider(
                    modifier = Modifier.testTag(testTag)
                )
            }

            with(onNodeWithTag(testTag)) {
                assertExists()
                assertIsDisplayed()
            }
        }
    }
}
