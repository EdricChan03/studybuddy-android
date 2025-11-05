package com.edricchan.studybuddy.ui.widgets.views.markdown

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performFirstLinkClick
import androidx.compose.ui.text.LinkAnnotation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.filters.SmallTest
import com.edricchan.studybuddy.ui.widgets.compose.markdown.SampleMarkdownText
import com.edricchan.studybuddy.ui.widgets.views.markdown.test.TestMarkdownViewActivity
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.instanceOf
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Ignore
import kotlin.test.Test

@Suppress("DEPRECATION")
@SmallTest
class MarkdownViewTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<TestMarkdownViewActivity>()

    private inline fun onMarkdownView(crossinline action: (MarkdownView) -> Unit) {
        composeTestRule.activityRule.scenario.onActivity {
            action(it.markdownView)
        }
    }

    private inline fun withMarkdownView(crossinline action: MarkdownView.() -> Unit) {
        composeTestRule.activityRule.scenario.onActivity {
            action(it.markdownView)
        }
    }

    private inline fun withMarkdownViewInteraction(block: ViewInteraction.() -> Unit) {
        with(onView(instanceOf(MarkdownView::class.java)), block)
    }

    @BeforeTest
    fun setUp() {
        onMarkdownView {
            it.markdownText = SampleMarkdownText
        }
    }

    @Test
    fun renders_markdownText() {
        withMarkdownViewInteraction {
            check(
                matches(
                    allOf(isDisplayed())
                )
            )
            with(
                composeTestRule.onNodeWithText(
                    "Finish the Compose rewrite for the tasks feature :tada:"
                )
            ) {
                assertExists()
                assertIsDisplayed()
            }
        }
    }

    @Test
    fun setMarkdownText_updatesText() {
        withMarkdownView {
            markdownText = "# Hello Markdown!"
        }
        withMarkdownViewInteraction {
            check(matches(isDisplayed()))
            with(composeTestRule.onNodeWithText("Hello Markdown!")) {
                assertExists()
                assertIsDisplayed()
            }
        }
    }

    @Ignore(
        "onLinkClick doesn't get invoked, see " +
            "https://github.com/boswelja/compose-markdown/issues/103"
    )
    @Test
    fun clickListeners_invokedWhenLinkClicked() {
        val listener: (LinkAnnotation) -> Unit = mockk(relaxed = true)
        withMarkdownView {
            addOnLinkClickListener(listener = listener)
        }
        withMarkdownViewInteraction {
            composeTestRule.onNodeWithText("Link").performFirstLinkClick()
        }
        // Clean-up
        withMarkdownView {
            removeOnLinkClickListener(listener = listener)
        }
        verify { listener(match<LinkAnnotation.Url> { it.url == "https://example.com" }) }
    }
}
