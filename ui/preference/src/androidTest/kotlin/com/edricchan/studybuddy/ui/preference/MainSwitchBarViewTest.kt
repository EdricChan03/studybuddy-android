package com.edricchan.studybuddy.ui.preference

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.isNotChecked
import androidx.test.espresso.matcher.ViewMatchers.isNotEnabled
import androidx.test.filters.SmallTest
import com.edricchan.studybuddy.ui.preference.mainswitch.MainSwitchBarView
import com.edricchan.studybuddy.ui.preference.mainswitch.OnMainSwitchChangeListener
import com.edricchan.studybuddy.ui.preference.mainswitch.restoreState
import com.edricchan.studybuddy.ui.preference.mainswitch.saveState
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.instanceOf
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@SmallTest
class MainSwitchBarViewTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<TestMainSwitchBarViewActivity>()

    @Test
    fun mainSwitchBar_click() {
        with(onView(instanceOf(MainSwitchBarView::class.java))) {
            check(matches(allOf(isEnabled(), isNotChecked(), isClickable())))
            perform(click())
            check(matches(allOf(isEnabled(), isChecked(), isClickable())))
            perform(click())
            check(matches(allOf(isEnabled(), isNotChecked(), isClickable())))
        }
    }

    @Test
    fun mainSwitchBar_canBeDisabled() {
        composeTestRule.activityRule.scenario.onActivity {
            it.switchBar.isEnabled = false
        }

        with(onView(instanceOf(MainSwitchBarView::class.java))) {
            check(matches(allOf(isNotEnabled(), isNotChecked())))
            perform(click())
            check(matches(allOf(isNotEnabled(), isNotChecked())))
        }
    }

    @Test
    fun mainSwitchBar_checkedChange() {
        val listener = OnMainSwitchChangeListener { switch, isChecked ->
            assertNull(switch)
            assertEquals(isChecked, composeTestRule.activity.switchBar.isChecked)
        }

        composeTestRule.activityRule.scenario.onActivity {
            it.switchBar.addOnCheckedChangeListener(listener)
        }

        with(onView(instanceOf(MainSwitchBarView::class.java))) {
            check(matches(allOf(isNotChecked())))
            perform(click())
            check(matches(allOf(isChecked())))
            perform(click())
            check(matches(allOf(isNotChecked())))
        }

        composeTestRule.activityRule.scenario.onActivity {
            // Clean-up
            it.switchBar.removeOnCheckedChangeListener(listener)
        }
    }

    @Test
    fun mainSwitchBar_saveState() {
        val switchTitle = "Hello switch bar"

        composeTestRule.activityRule.scenario.onActivity {
            with(it.switchBar) {
                isEnabled = false
                isChecked = true
                title = switchTitle
            }

            it.switchBar.saveState(null).apply {
                assertEquals(title, it.switchBar.title)
                assertEquals(checked, it.switchBar.isChecked)
                assertEquals(enabled, it.switchBar.isEnabled)
            }
        }

    }

    @Test
    fun mainSwitchBar_restoreState() {
        val switchTitle = "Hello switch bar"

        val savedState = MainSwitchBarView.SavedState(
            enabled = false, checked = true, title = switchTitle, superState = null
        )

        composeTestRule.activityRule.scenario.onActivity {
            with(it.switchBar) {
                restoreState(savedState)

                assertEquals(isEnabled, savedState.enabled)
                assertEquals(isChecked, savedState.checked)
                assertEquals(title, savedState.title)
            }
        }
    }
}
