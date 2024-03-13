package com.edricchan.studybuddy.ui.widgets.compose.list

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertHasNoClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelectable
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme
import com.edricchan.studybuddy.ui.widgets.compose.assertExistsAndIsDisplayed
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertTrue

class RadioButtonListItemTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Suppress("TestFunctionName")
    @Composable
    private fun ListItemText() = Text(modifier = textModifier, text = itemText)

    @Suppress("TestFunctionName")
    @Composable
    private fun ListItemTrailingContent() = Icon(
        modifier = trailingContentModifier,
        imageVector = Icons.Outlined.Favorite,
        contentDescription = null
    )

    private val itemText = "Item 1"

    @Test
    fun radioButtonListItem() {
        var selected by mutableStateOf(false)
        composeTestRule.apply {
            setContent {
                StudyBuddyTheme {
                    RadioButtonListItem(
                        modifier = listItemModifier,
                        radioButtonModifier = radioButtonModifier,
                        selected = selected,
                        onSelected = { selected = true }
                    ) {
                        ListItemText()
                    }
                }
            }

            val listItem = onNodeWithTag(listItemTag).apply {
                assertExists()
                assertIsDisplayed()
                assertHasClickAction()
                assertIsSelectable()
                assertIsEnabled()
                assertIsNotSelected()
            }

            onNodeWithTag(textTag, useUnmergedTree = true).apply {
                assertExists()
                assertIsDisplayed()
            }

            onNodeWithTag(radioButtonTag, useUnmergedTree = true).apply {
                assertExists()
                assertIsDisplayed()
                assertHasNoClickAction()
            }

            listItem.apply {
                performClick()
                assertTrue(selected, "Item is selected")
                assertIsSelected()

                performClick()
                assertTrue(selected, "Item is selected")
                assertIsSelected()
            }
        }
    }

    @Test
    fun radioButtonListItem_withTrailingContent() {
        composeTestRule.apply {
            setContent {
                StudyBuddyTheme {
                    RadioButtonListItem(
                        modifier = listItemModifier,
                        radioButtonModifier = radioButtonModifier,
                        selected = false,
                        onSelected = {},
                        text = { ListItemText() },
                        trailingContent = { ListItemTrailingContent() }
                    )
                }
            }

            onNodeWithTag(listItemTag).assertExistsAndIsDisplayed()
            onNodeWithTag(radioButtonTag, useUnmergedTree = true).assertExistsAndIsDisplayed()
            onNodeWithTag(textTag, useUnmergedTree = true).assertExistsAndIsDisplayed()
            onNodeWithTag(trailingContentTag, useUnmergedTree = true).assertExistsAndIsDisplayed()
        }
    }

    @Test
    fun radioButtonListItem_disabled() {
        var selected by mutableStateOf(false)
        composeTestRule.apply {
            setContent {
                StudyBuddyTheme {
                    RadioButtonListItem(
                        modifier = listItemModifier,
                        radioButtonModifier = radioButtonModifier,
                        selected = selected,
                        onSelected = {},
                        text = { ListItemText() },
                        enabled = false
                    )
                }
            }

            val listItem = onNodeWithTag(listItemTag).apply {
                assertExists()
                assertIsNotEnabled()
            }

            onNodeWithTag(radioButtonTag, useUnmergedTree = true).apply {
                assertExists()
            }

            selected = true
            listItem.assertIsSelected()
        }
    }
}
