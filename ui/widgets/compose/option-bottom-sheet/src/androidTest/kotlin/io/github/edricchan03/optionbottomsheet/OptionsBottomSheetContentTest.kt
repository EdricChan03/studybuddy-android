package io.github.edricchan03.optionbottomsheet

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsSelectable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme
import io.github.edricchan03.optionbottomsheet.models.BottomSheetOption
import io.github.edricchan03.optionbottomsheet.models.BottomSheetOptionGroup
import org.junit.Ignore
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class OptionsBottomSheetContentTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private enum class Item {
        One,
        Two,
        Three,
        Four
    }

    private val headerTag = "HeaderTitle"
    private val contentTag = "Content"

    @Test
    fun content() {
        val headerTitle = "Header title"
        var called = false

        composeTestRule.apply {
            setContent {
                StudyBuddyTheme {
                    OptionsBottomSheetContent(
                        onDismissBottomSheetRequest = { called = true }, // No-op
                        group = BottomSheetOptionGroup(
                            title = headerTitle,
                            items = Item.entries.map {
                                BottomSheetOption(
                                    id = it.ordinal,
                                    title = it.name
                                )
                            }
                        )
                    )
                }
            }

            onNodeWithText(headerTitle).apply {
                assertExists()
                assertIsDisplayed()
            }

            Item.entries.forEach {
                onNodeWithText(it.name).apply {
                    assertExists()
                    assertIsDisplayed()
                    assertHasClickAction()
                    assertIsEnabled()

                    assertFalse(called)
                    performClick()
                    assertTrue(called)
                    // Reset the state
                    called = false
                }
            }
        }
    }

    @Test
    fun content_nullHeaderTitle() {
        composeTestRule.apply {
            setContent {
                StudyBuddyTheme {
                    OptionsBottomSheetContent(
                        headerModifier = Modifier.testTag(headerTag),
                        onDismissBottomSheetRequest = {}, // No-op
                        group = BottomSheetOptionGroup(
                            title = null,
                            items = listOf()
                        )
                    )
                }
            }

            onNodeWithTag(headerTag).assertDoesNotExist()
        }
    }

    @Ignore("Selection state logic to be added")
    @Test
    fun content_singleCheckableBehavior() {
        val items = Item.entries.map {
            BottomSheetOption(id = it.ordinal, title = it.name)
        }
        composeTestRule.apply {
            setContent {
                StudyBuddyTheme {
                    OptionsBottomSheetContent(
                        modifier = Modifier.testTag(contentTag),
                        onDismissBottomSheetRequest = {}, // No-op
                        group = BottomSheetOptionGroup(
                            title = "Header title",
                            items = items,
                            checkableBehavior = BottomSheetOptionGroup.CheckableBehavior.Single
                        )
                    )
                }
            }

            onNodeWithTag(contentTag).assert(SemanticsMatcher.keyIsDefined(SemanticsProperties.SelectableGroup))

            items.forEach { option ->
                onNodeWithText(option.title).apply {
                    assertExists()
                    assertIsDisplayed()
                    assertHasClickAction()
                    assertIsSelectable()
                }
                // TODO: Test selection status
            }
        }
    }
}
