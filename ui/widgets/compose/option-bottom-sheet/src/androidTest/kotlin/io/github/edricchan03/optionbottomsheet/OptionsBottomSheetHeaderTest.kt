package io.github.edricchan03.optionbottomsheet

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
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
                    OptionsBottomSheetHeader(title = headerTitle)
                }
            }

            onNodeWithText(headerTitle).apply {
                assertExists()
                assertIsDisplayed()
            }
        }
    }

    @Test
    fun header_nullText() {
        val headerTitle = null
        val tag = "HeaderTitle"
        composeTestRule.apply {
            setContent {
                StudyBuddyTheme {
                    OptionsBottomSheetHeader(
                        modifier = Modifier.testTag(tag),
                        title = headerTitle
                    )
                }
            }

            onRoot().onChildren().apply {
                assertCountEquals(0)
            }
            onNodeWithTag(tag).assertDoesNotExist()
        }
    }
}
