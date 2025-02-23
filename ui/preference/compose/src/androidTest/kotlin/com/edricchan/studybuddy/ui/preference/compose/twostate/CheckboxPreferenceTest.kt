package com.edricchan.studybuddy.ui.preference.compose.twostate

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

class CheckboxPreferenceTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun kitchenSinkTest() {
        var checked by mutableStateOf(false)

        composeTestRule.apply {
            setContent {
                CheckboxPreference(
                    modifier = Modifier.testTag(testTag),
                    icon = {
                        Icon(
                            modifier = Modifier.testTag(iconTag),
                            imageVector = Icons.Outlined.Build,
                            contentDescription = null
                        )
                    },
                    title = { Text(text = "Checkbox item") },
                    checked = checked,
                    onCheckedChange = { checked = it }
                )
            }

            with(onNodeWithTag(ActionCheckboxTestTag, useUnmergedTree = true)) {
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

                checked = true

                assertIsOn()

                // Uncheck the checkbox
                performClick()
                assertIsOff()
            }
        }
    }

    @Test
    fun disabledTest() {
        var checked by mutableStateOf(false)
        composeTestRule.apply {
            setContent {
                CheckboxPreference(
                    modifier = Modifier.testTag(testTag),
                    enabled = false,
                    title = { Text(text = "Checkbox item") },
                    checked = checked,
                    onCheckedChange = { checked = it }
                )
            }

            with(onNodeWithTag(testTag)) {
                assertExists()
                assertIsDisplayed()
                assertIsNotEnabled()
                assertIsOff()

                checked = true
                assertIsOn()

                performClick()
                // Verify that the checked status shouldn't be toggled
                assertIsOn()
            }
        }
    }
}
