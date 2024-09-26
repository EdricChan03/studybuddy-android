package com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select

import android.content.Context
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.fragment.app.testing.EmptyFragmentActivity
import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.withFragment
import androidx.test.core.app.launchActivity
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.dsl.showSingleSelectBottomSheet
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.dsl.singleSelectGroup
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model.OptionBottomSheetItem
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

// TODO: This needs moar tests. Moooaaaarrr!
@MediumTest
class SingleSelectBottomSheetFragmentTest {
    @get:Rule
    val composeTestRule = createEmptyComposeRule()

    private lateinit var targetContext: Context

    private lateinit var cancelStr: String
    private lateinit var confirmStr: String

    @BeforeTest
    fun setUp() {
        targetContext = InstrumentationRegistry.getInstrumentation().targetContext

        cancelStr = targetContext.getString(android.R.string.cancel)
        confirmStr = targetContext.getString(android.R.string.ok)
    }

    @Test
    fun singleFragment_creates() {
        launchFragment<SelectBottomSheetFragment<Any>>(
            fragmentArgs = SelectBottomSheetFragment.getArguments<Any>(
                itemsData = singleSelectGroup {},
                headerTitle = "Select something"
            ),
            themeResId = com.google.android.material.R.style.Theme_Material3_DayNight
        ).use { scenario ->
            scenario.withFragment {
                assertNotNull(dialog) {
                    assertTrue(it.isShowing)
                }
            }
        }
    }

    @Test
    fun singleFragment_confirm() {
        val title1 = "Unselected item"
        val title2 = "Selected item"
        launchFragment<SelectBottomSheetFragment<Any>>(
            fragmentArgs = SelectBottomSheetFragment.getArguments(
                itemsData = singleSelectGroup {
                    addItem(1, title1, false)
                    addItem(2, title2, true)
                },
                headerTitle = "Select something"
            )
        ).use {
            with(composeTestRule) {
                with(onNodeWithText(confirmStr)) {
                    assertExists()
                    performClick()
                }

                // Fragment should be dismissed
                with(onNodeWithText("Select something")) {
                    assertDoesNotExist()
                }
            }
        }
    }

    @Test
    fun singleFragment_resultConfirm() {
        val onCanceled: () -> Unit = mockk(relaxed = true)
        val onConfirm: (selectedItems: OptionBottomSheetItem<Int>) -> Unit = mockk(relaxed = true)

        launchActivity<EmptyFragmentActivity>().use { scenario ->
            scenario.onActivity { activity ->
                activity.showSingleSelectBottomSheet<Int>(
                    "Select something",
                    onCanceled = onCanceled,
                    onConfirm = onConfirm
                ) {
                    addItem(
                        id = 1,
                        title = "Item 1",
                        selected = true
                    )

                    addItem(
                        id = 2,
                        title = "Item 2",
                        selected = false
                    )
                }
            }

            with(composeTestRule) {
//                    onRoot().printToLog("SelectTest_confirm")
                onNodeWithText("Item 2").performClick()
                onNodeWithText(confirmStr).performClick()
            }

            verify(exactly = 0) { onCanceled() }
            verify(exactly = 1) { onConfirm(match { it.id == 2 }) }
        }
    }

    @Test
    fun singleFragment_resultCancel() {
        val onCanceled: () -> Unit = mockk(relaxed = true)
        val onConfirm: (selectedItems: OptionBottomSheetItem<*>) -> Unit = mockk(relaxed = true)

        launchActivity<EmptyFragmentActivity>().use { scenario ->
            scenario.onActivity { activity ->
                activity.showSingleSelectBottomSheet<Int>(
                    "Select something",
                    onCanceled = onCanceled,
                    onConfirm = onConfirm
                ) {
                    addItem(
                        id = 1,
                        title = "Item 1",
                        selected = true
                    )

                    addItem(
                        id = 2,
                        title = "Item 2",
                        selected = false
                    )
                }
            }

            with(composeTestRule) {
                onNodeWithText("Item 2").performClick()
                onNodeWithText(cancelStr).performClick()
            }

            verify(exactly = 1) { onCanceled() }
            verify(exactly = 0) { onConfirm(any()) } // This should never be called
        }
    }
}
