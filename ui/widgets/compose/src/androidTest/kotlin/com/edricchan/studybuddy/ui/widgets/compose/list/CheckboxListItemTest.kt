package com.edricchan.studybuddy.ui.widgets.compose.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertHasNoClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertIsToggleable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CheckboxListItemTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun checkboxListItem() {
        var checked by mutableStateOf(false)
        composeTestRule.apply {
            setContent {
                StudyBuddyTheme {
                    CheckboxListItem(
                        modifier = listItemModifier,
                        checkboxModifier = checkboxModifier,
                        checked = checked,
                        onCheckedChange = { checked = it }
                    ) {
                        ListItemText()
                    }
                }
            }

            val listItem = onNodeWithTag(listItemTag).apply {
                assertExists()
                assertIsDisplayed()
                assertHasClickAction()
                assertIsToggleable()
                assertIsEnabled()
                assertIsOff()
            }

            onNodeWithTag(textTag, useUnmergedTree = true).apply {
                assertExists()
                assertIsDisplayed()
            }

            onNodeWithTag(checkboxTag, useUnmergedTree = true).apply {
                assertExists()
                assertIsDisplayed()
                assertHasNoClickAction()
            }

            listItem.apply {
                performClick()
                assertTrue(checked, "Item is checked")
                assertIsOn()

                performClick()
                assertFalse(checked, "Item is unchecked")
                assertIsOff()
            }
        }
    }

    @Test
    fun checkboxListItem_withTrailingContent() {
        composeTestRule.apply {
            setContent {
                StudyBuddyTheme {
                    CheckboxListItem(
                        modifier = listItemModifier,
                        checkboxModifier = checkboxModifier,
                        checked = false,
                        onCheckedChange = {},
                        text = { ListItemText() },
                        trailingContent = { ListItemTrailingContent() }
                    )
                }
            }

            onNodeWithTag(listItemTag).assertExistsAndIsDisplayed()
            onNodeWithTag(checkboxTag, useUnmergedTree = true).assertExistsAndIsDisplayed()
            onNodeWithTag(textTag, useUnmergedTree = true).assertExistsAndIsDisplayed()
            onNodeWithTag(trailingContentTag, useUnmergedTree = true).assertExistsAndIsDisplayed()
        }
    }

    @Test
    fun checkboxListItem_disabled() {
        var checked by mutableStateOf(false)
        composeTestRule.apply {
            setContent {
                StudyBuddyTheme {
                    CheckboxListItem(
                        modifier = listItemModifier,
                        checkboxModifier = checkboxModifier,
                        checked = checked,
                        onCheckedChange = {},
                        text = { ListItemText() },
                        enabled = false
                    )
                }
            }

            val listItem = onNodeWithTag(listItemTag).apply {
                assertExists()
                assertIsNotEnabled()
            }

            onNodeWithTag(checkboxTag, useUnmergedTree = true).apply {
                assertExists()
            }

            checked = true
            listItem.assertIsOn()
        }
    }
}
