package com.edricchan.studybuddy.ui.widgets.compose

import androidx.activity.ComponentActivity
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.printToLog
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class BackIconButtonTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var tooltipContentDesc: String
    private lateinit var iconButtonMatcher: SemanticsMatcher

    @BeforeTest
    fun setup() {
        tooltipContentDesc = composeTestRule.activity.getString(R.string.back_btn_tooltip_text)
        iconButtonMatcher = hasContentDescription(tooltipContentDesc)
    }

    @Test
    fun backIconButton_click() {
        var clicked = false

        composeTestRule.apply {
            setContent {
                StudyBuddyTheme {
                    BackIconButton(onClick = { clicked = true })
                }
            }

            onNode(iconButtonMatcher).apply {
                assertExists()
                assertIsDisplayed()
                assertHasClickAction()

                performClick()
            }
        }

        assertTrue(clicked, "Button should be clicked")
    }

    @Test
    fun backIconButton_longClick() {
        val tooltipTag = "TooltipPopup"
        val tooltipTextTag = "TooltipText"

        composeTestRule.apply {
            setContent {
                StudyBuddyTheme {
                    BackIconButton(
                        tooltipModifier = Modifier.testTag(tooltipTag),
                        tooltipTextModifier = Modifier.testTag(tooltipTextTag),
                        onClick = { /* no-op */ }
                    )
                }
            }

            mainClock.autoAdvance = false

            onNode(iconButtonMatcher).apply {
                assertExists()
                assertIsDisplayed()
                performTouchInput {
                    longClick()
                }
            }

            // Advance by the fade in time
            mainClock.advanceTimeBy(150L)
            waitForIdle()

            onAllNodes(isRoot(), useUnmergedTree = true).printToLog(
                "BackIconButton",
                maxDepth = Int.MAX_VALUE
            )

            onNodeWithTag(tooltipTag).apply {
                assertIsDisplayed() // Long-pressing the icon button should display the tooltip
            }
            onNodeWithTag(tooltipTextTag).apply {
                assertIsDisplayed()
                assertTextEquals(tooltipContentDesc)
            }
        }
    }
}
