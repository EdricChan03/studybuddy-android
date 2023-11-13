package io.github.edricchan03.optionbottomsheet.items

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertIsToggleable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme
import io.github.edricchan03.optionbottomsheet.OptionsBottomSheetCheckableItem
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class OptionsBottomSheetCheckableItemTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun checkableItem() {
        composeTestRule.apply {
            val itemText = "Item 1"
            var checked by mutableStateOf(false)
            setContent {
                StudyBuddyTheme {
                    OptionsBottomSheetCheckableItem(
                        title = itemText,
                        checked = checked,
                        onCheckedChange = { checked = it }
                    )
                }
            }
            val item = onNodeWithText(itemText).apply {
                assertExists()
                assertIsToggleable()
                assertIsEnabled()
                assertIsOff()
                performClick()
            }
            assertTrue(checked, "State should be checked")
            item.apply {
                assertIsOn()
                performClick()
            }
            assertFalse(checked, "State should be unchecked")
        }
    }

    // TODO: Add test that the colours are disabled
    @Test
    fun checkableItem_disabled() {
        composeTestRule.apply {
            val itemText = "Item 1"
            var checked by mutableStateOf(false)
            setContent {
                StudyBuddyTheme {
                    OptionsBottomSheetCheckableItem(
                        title = itemText,
                        checked = checked,
                        onCheckedChange = { checked = it },
                        enabled = false
                    )
                }
            }
            onNodeWithText(itemText).apply {
                assertExists()
                assertIsToggleable()
                assertIsNotEnabled()
                performClick()
                assertIsOff()
            }
            assertFalse(checked, "State should be unchecked")
        }
    }
}
