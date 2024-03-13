package com.edricchan.studybuddy.ui.preference.compose.twostate

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Rule
import kotlin.test.Test

private const val testTag = "CheckboxPreferenceTag"
private const val iconTag = "IconTag"

class SwitchPreferenceTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun kitchenSinkTest() {
        val (checked, onChecked) = mutableStateOf(false)

        composeTestRule.apply {
            setContent {
                SwitchPreference(
                    modifier = Modifier.testTag(testTag),
                    icon = {
                        Icon(
                            modifier = Modifier.testTag(iconTag),
                            imageVector = Icons.Outlined.Info,
                            contentDescription = null
                        )
                    },
                    title = { Text(text = "Switch item") },
                    checked = checked,
                    onCheckedChange = onChecked
                )
            }

            with(onNodeWithTag(ActionSwitchTestTag, useUnmergedTree = true)) {
                assertExists()
                assertIsDisplayed()
            }

            with(onNodeWithTag(iconTag, useUnmergedTree = true)) {
                assertExists()
                assertIsDisplayed()
            }

            with(onNodeWithTag(testTag)) {
                assertExists()
                assertIsDisplayed()
                assertIsEnabled()
                assertIsOff()

                onChecked(true)

                assertIsOn()

                // Uncheck the switch
                performClick()
                assertIsOff()
            }
        }
    }

    @Test
    fun disabledTest() {
        val (checked, onChecked) = mutableStateOf(false)

        composeTestRule.apply {
            setContent {
                CheckboxPreference(
                    modifier = Modifier.testTag(testTag),
                    enabled = false,
                    title = { Text(text = "Switch item") },
                    checked = checked,
                    onCheckedChange = onChecked
                )
            }

            with(onNodeWithTag(testTag)) {
                assertExists()
                assertIsDisplayed()
                assertIsNotEnabled()
                assertIsOff()

                onChecked(true)
                assertIsOn()

                performClick()
                // Verify that the checked status shouldn't be toggled
                assertIsOn()
            }
        }
    }
}
