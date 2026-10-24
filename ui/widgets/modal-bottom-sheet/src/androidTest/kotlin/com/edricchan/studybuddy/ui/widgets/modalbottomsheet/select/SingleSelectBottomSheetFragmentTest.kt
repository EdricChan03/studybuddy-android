package com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select

import android.content.Context
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.launchActivity
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.edricchan.studybuddy.core.di.testing.HiltTestActivity
import com.edricchan.studybuddy.core.di.testing.fragment.launchHiltFragment
import com.edricchan.studybuddy.core.di.testing.fragment.launchHiltFragmentInContainer
import com.edricchan.studybuddy.core.settings.appearance.di.AppearanceSettingsModule
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.dsl.showSingleSelectBottomSheet
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.dsl.singleSelectGroup
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model.OptionBottomSheetItem
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model.intIdSerializer
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model.serializedIdOrNull
import com.google.android.material.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

// TODO: This needs moar tests. Moooaaaarrr!
@MediumTest
@HiltAndroidTest
@UninstallModules(
    value = [AppearanceSettingsModule::class]
)
class SingleSelectBottomSheetFragmentTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

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
        launchHiltFragment<SelectBottomSheetFragment>(
            fragmentArgs = SelectBottomSheetFragment.getArguments(
                itemsData = singleSelectGroup(
                    idSerializer = intIdSerializer()
                ) {},
                headerTitle = "Select something"
            ),
            themeResId = R.style.Theme_Material3_DayNight,
        ) {
            assertNotNull(dialog) {
                assertTrue(it.isShowing)
            }
        }
    }

    @Test
    fun singleFragment_confirm() {
        val title1 = "Unselected item"
        val title2 = "Selected item"
        launchHiltFragmentInContainer<SelectBottomSheetFragment>(
            fragmentArgs = SelectBottomSheetFragment.getArguments(
                itemsData = singleSelectGroup(
                    idSerializer = intIdSerializer()
                ) {
                    addItem(1, title1, false)
                    addItem(2, title2, true)
                },
                headerTitle = "Select something"
            )
        ) {
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
        val onConfirm: (selectedItems: OptionBottomSheetItem) -> Unit = mockk(relaxed = true)

        val idSerializer = intIdSerializer()

        launchActivity<HiltTestActivity>().use { scenario ->
            scenario.onActivity { activity ->
                activity.showSingleSelectBottomSheet(
                    idSerializer = idSerializer,
                    headerTitle = "Select something",
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
            with(idSerializer) {
                verify(exactly = 1) { onConfirm(match { it.serializedIdOrNull() == 2 }) }
            }
        }
    }

    @Test
    fun singleFragment_resultCancel() {
        val onCanceled: () -> Unit = mockk(relaxed = true)
        val onConfirm: (selectedItems: OptionBottomSheetItem) -> Unit = mockk(relaxed = true)

        launchActivity<HiltTestActivity>().use { scenario ->
            scenario.onActivity { activity ->
                activity.showSingleSelectBottomSheet(
                    idSerializer = intIdSerializer(),
                    headerTitle = "Select something",
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
