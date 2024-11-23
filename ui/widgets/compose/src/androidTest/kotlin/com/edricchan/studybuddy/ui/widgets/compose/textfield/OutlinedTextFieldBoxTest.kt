package com.edricchan.studybuddy.ui.widgets.compose.textfield

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertHasNoClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme
import com.edricchan.studybuddy.ui.widgets.compose.assertExistsAndIsDisplayed
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertTrue

class OutlinedTextFieldBoxTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun outlinedTextFieldBox() {
        composeTestRule.apply {
            setContent {
                StudyBuddyTheme {
                    OutlinedTextFieldBox(
                        modifier = boxModifier,
                        headlineText = { OutlinedTextFieldBoxText() },
                        leadingIcon = { OutlinedTextFieldBoxLeadingIcon() },
                        trailingIcon = { OutlinedTextFieldBoxTrailingIcon() }
                    )
                }
            }

            onNodeWithTag(boxTag).apply {
                assertExists()
                assertIsDisplayed()
                assertHasNoClickAction()
            }

            onNodeWithTag(textTag, useUnmergedTree = true).assertExistsAndIsDisplayed()

            onNodeWithTag(leadingIconTag, useUnmergedTree = true).assertExistsAndIsDisplayed()

            onNodeWithTag(trailingIconTag, useUnmergedTree = true).assertExistsAndIsDisplayed()
        }
    }

    @Test
    fun outlinedTextFieldBox_withOnClickAndEnabled() {
        var hasClicked = false
        var enabled by mutableStateOf(true)

        composeTestRule.apply {
            setContent {
                StudyBuddyTheme {
                    OutlinedTextFieldBox(
                        modifier = boxModifier,
                        onClick = { hasClicked = true },
                        enabled = enabled,
                        headlineText = { OutlinedTextFieldBoxText() },
                        leadingIcon = { OutlinedTextFieldBoxLeadingIcon() },
                        trailingIcon = { OutlinedTextFieldBoxTrailingIcon() }
                    )
                }
            }

            val box = onNodeWithTag(boxTag).apply {
                assertExists()
                assertIsDisplayed()
                assertIsEnabled()
                assertHasClickAction()
                performClick()
                assertTrue(hasClicked, "Composable should have been clicked")
            }

            onNodeWithTag(textTag, useUnmergedTree = true).assertExistsAndIsDisplayed()
            onNodeWithTag(leadingIconTag, useUnmergedTree = true).assertExistsAndIsDisplayed()
            onNodeWithTag(trailingIconTag, useUnmergedTree = true).assertExistsAndIsDisplayed()

            enabled = false
            box.assertIsNotEnabled()
        }
    }
}
