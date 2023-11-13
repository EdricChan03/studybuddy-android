package io.github.edricchan03.optionbottomsheet.items

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme
import io.github.edricchan03.optionbottomsheet.OptionsBottomSheetItem
import org.junit.Rule
import kotlin.test.Test

class OptionsBottomSheetItemTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private enum class BottomSheetItem {
        One,
        Two,
        Three
    }

    @Test
    fun item() {
        var currItem = BottomSheetItem.Three
        composeTestRule.apply {
            setContent {
                StudyBuddyTheme {
                    LazyColumn {
                        items(BottomSheetItem.entries) {
                            OptionsBottomSheetItem(title = it.name, onClick = { currItem = it })
                        }
                    }
                }
            }

            BottomSheetItem.entries.forEach {
                onNodeWithText(it.name).apply {
                    assertIsDisplayed()
                    assertIsEnabled()
                    performClick()
                }
                assert(currItem == it) { "Current item should be updated to $it" }
            }
        }
    }

    @Test
    fun item_disabled() {
        var currItem = BottomSheetItem.Two
        composeTestRule.apply {
            setContent {
                StudyBuddyTheme {
                    LazyColumn {
                        items(BottomSheetItem.entries) {
                            OptionsBottomSheetItem(
                                title = it.name,
                                onClick = { currItem = it },
                                enabled = false
                            )
                        }
                    }
                }
            }

            BottomSheetItem.entries.forEach {
                onNodeWithText(it.name).apply {
                    assertIsDisplayed()
                    assertIsNotEnabled()
                    performClick()
                }
                assert(currItem == BottomSheetItem.Two) {
                    "Current item should not be updated to $it"
                }
            }
        }
    }
}
