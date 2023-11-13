package io.github.edricchan03.optionbottomsheet.items

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsSelectable
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme
import io.github.edricchan03.optionbottomsheet.OptionsBottomSheetSelectableItem
import org.junit.Rule
import kotlin.test.Test

class OptionsBottomSheetSelectableItemTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private enum class SelectedItem {
        One,
        Two,
        Three
    }

    @Test
    fun selectableItem() {
        composeTestRule.apply {
            var selectedItem by mutableStateOf(SelectedItem.One)
            setContent {
                StudyBuddyTheme {
                    LazyColumn(
                        modifier = Modifier.selectableGroup()
                    ) {
                        items(SelectedItem.entries) {
                            OptionsBottomSheetSelectableItem(
                                title = it.name,
                                selected = it == selectedItem,
                                onClick = { selectedItem = it }
                            )
                        }
                    }
                }
            }

            SelectedItem.entries.forEach {
                onNodeWithText(it.name).apply {
                    assertIsDisplayed()
                    assertIsSelectable()
                    assertIsEnabled()
                    performClick()
                    assertIsSelected()
                }
                assert(selectedItem == it) { "Selected item should be updated to $it" }
            }
        }
    }

    @Test
    fun selectableItem_disabled() {
        composeTestRule.apply {
            var selectedItem = SelectedItem.One
            setContent {
                StudyBuddyTheme {
                    LazyColumn(
                        modifier = Modifier.selectableGroup()
                    ) {
                        items(SelectedItem.entries) {
                            OptionsBottomSheetSelectableItem(
                                title = it.name,
                                selected = it == selectedItem,
                                onClick = { selectedItem = it },
                                enabled = false
                            )
                        }
                    }
                }
            }

            SelectedItem.entries.forEach {
                onNodeWithText(it.name).apply {
                    assertIsDisplayed()
                    assertIsSelectable()
                    assertIsNotEnabled()
                    performClick()
                }
                assert(selectedItem == SelectedItem.One) { "Selected item should not be updated" }
            }
        }
    }
}
