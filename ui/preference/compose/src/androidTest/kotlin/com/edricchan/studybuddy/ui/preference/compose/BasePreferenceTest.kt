package com.edricchan.studybuddy.ui.preference.compose

import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertHasNoClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import kotlin.test.Test

private const val testTag = "PreferenceTag"
private const val text = "Preference text"

class BasePreferenceTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun clickableTest() {
        composeTestRule.apply {
            setContent {
                Preference(
                    modifier = Modifier.testTag(testTag),
                    onClick = { /*TODO*/ },
                    title = { Text(text = text) }
                )
            }

            with(onNodeWithTag(testTag)) {
                assertExists()
                assertIsDisplayed()
                assertIsEnabled()
                assertHasClickAction()
                assertTextContains(text)
            }
        }
    }

    @Test
    fun disabledTest() {
        composeTestRule.apply {
            setContent {
                Preference(
                    modifier = Modifier.testTag(testTag),
                    enabled = false,
                    onClick = { /*TODO*/ },
                    title = { Text(text = text) }
                )
            }

            with(onNodeWithTag(testTag)) {
                assertExists()
                assertIsDisplayed()
                assertIsNotEnabled()
                assertHasNoClickAction()
                assertTextContains(text)
            }
        }
    }

    @Test
    fun actionTest() {
        val actionTag = "ActionTag"
        val actionText = "Action text"

        composeTestRule.apply {
            setContent {
                Preference(
                    modifier = Modifier.testTag(testTag),
                    title = { Text(text = text) },
                    action = {
                        Text(
                            modifier = Modifier.testTag(actionTag),
                            text = actionText
                        )
                    },
                    showActionDivider = true
                )
            }

            with(onNodeWithTag(testTag)) {
                assertExists()
                assertIsDisplayed()
                assertTextContains(text)
            }

            with(onNodeWithTag(actionTag)) {
                assertExists()
                assertIsDisplayed()
                assertTextContains(actionText)
            }
        }
    }

    @Test
    fun actionDividerTest() {
        composeTestRule.apply {
            setContent {
                Preference(
                    modifier = Modifier.testTag(testTag),
                    title = { Text(text = text) },
                    showActionDivider = true
                )
            }

            with(onNodeWithTag(testTag)) {
                assertExists()
                assertIsDisplayed()
                assertTextContains(text)
            }

            with(onNodeWithTag(ActionDividerTestTag)) {
                assertExists()
                assertIsDisplayed()
            }
        }
    }
}
