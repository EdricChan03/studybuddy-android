package com.edricchan.studybuddy.ui.preference.compose

import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import kotlin.test.Test

class PreferenceCategoryTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun noTitleTest() {
        val contentTag = "ContentTag"
        composeTestRule.apply {
            setContent {
                PreferenceCategory {
                    Preference(
                        modifier = Modifier.testTag(contentTag),
                        title = { Text(text = "Preference text 1") }
                    )
                }
            }

            with(onNodeWithTag(contentTag)) {
                assertExists()
                assertIsDisplayed()
            }
        }
    }

    @Test
    fun titleTest() {
        val titleTag = "TitleTag"
        val titleText = "Category title"
        val contentTag = "ContentTag"
        composeTestRule.apply {
            setContent {
                PreferenceCategory(
                    title = {
                        Text(
                            modifier = Modifier.testTag(titleTag),
                            text = titleText
                        )
                    }
                ) {
                    Preference(
                        modifier = Modifier.testTag(contentTag),
                        title = { Text(text = "Preference text 1") }
                    )
                }
            }

            with(onNodeWithTag(titleTag)) {
                assertExists()
                assertIsDisplayed()
            }

            with(onNodeWithTag(contentTag)) {
                assertExists()
                assertIsDisplayed()
            }
        }
    }
}
