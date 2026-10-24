package com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.testing.ViewModelScenario
import androidx.lifecycle.viewmodel.testing.viewModelScenario
import androidx.savedstate.serialization.encodeToSavedState
import androidx.test.filters.SmallTest
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.SelectBottomSheetFragment
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.dsl.singleSelectGroup
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model.OptionBottomSheetGroup
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model.intIdSerializer
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model.selectedItem
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

private fun createSelectVM(handleInit: SavedStateHandle.() -> Unit) = viewModelScenario {
    SelectBottomSheetViewModel(createSavedStateHandle().apply(handleInit))
}

private fun <R> useSelectVM(
    handleInit: SavedStateHandle.() -> Unit = {},
    scenarioBlock: (ViewModelScenario<SelectBottomSheetViewModel>) -> R
) = viewModelScenario {
    SelectBottomSheetViewModel(createSavedStateHandle().apply(handleInit))
}.use(scenarioBlock)

@SmallTest
class SelectBottomSheetViewModelTest {
    @Test
    fun toggleSelectedItem_updateState() {
        val itemsData: OptionBottomSheetGroup = singleSelectGroup(
            idSerializer = intIdSerializer()
        ) {
            (1..10).forEach {
                addItem(it, "Item $it")
            }

            selectedIndex = 0
        }

        useSelectVM(
            handleInit = {
                this[SelectBottomSheetFragment.TAG_ITEMS_DATA] = encodeToSavedState(itemsData)
            }
        ) { scenario ->
            with(scenario.viewModel) {
                assertEquals(items.value.items, itemsData.items)

                val newSelected = itemsData.items.first { it.id == "5" }

                toggleSelectedItem(newSelected, true)

                assertNotNull(items.value.selectedItem) {
                    assertEquals(it, newSelected)
                }
            }
        }
    }
}
