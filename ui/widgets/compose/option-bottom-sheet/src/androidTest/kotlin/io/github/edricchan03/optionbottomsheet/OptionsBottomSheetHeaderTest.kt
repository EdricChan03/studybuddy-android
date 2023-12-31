package io.github.edricchan03.optionbottomsheet

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme
import org.junit.Rule
import kotlin.test.Test

class OptionsBottomSheetHeaderTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun header() {
        val headerTitle = "Header title"
        composeTestRule.apply {
            setContent {
                StudyBuddyTheme {
                    OptionsBottomSheetDefaults.Header(title = headerTitle)
                }
            }

            onNodeWithText(headerTitle).apply {
                assertExists()
                assertIsDisplayed()
            }
        }
    }
}
