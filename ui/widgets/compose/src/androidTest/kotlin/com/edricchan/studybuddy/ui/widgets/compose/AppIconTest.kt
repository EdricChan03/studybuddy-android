package com.edricchan.studybuddy.ui.widgets.compose

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme
import org.junit.Rule
import kotlin.test.Test
import com.edricchan.studybuddy.core.resources.R as CoreResR

class AppIconTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val contentDesc by lazy { composeTestRule.activity.getString(CoreResR.string.app_logo_content_desc) }

    @Test
    fun appIcon() {
        composeTestRule.apply {
            setContent {
                StudyBuddyTheme {
                    AppIcon()
                }
            }

            onNodeWithContentDescription(contentDesc).apply {
                assertExists()
                assertIsDisplayed()
            }
        }
    }
}
