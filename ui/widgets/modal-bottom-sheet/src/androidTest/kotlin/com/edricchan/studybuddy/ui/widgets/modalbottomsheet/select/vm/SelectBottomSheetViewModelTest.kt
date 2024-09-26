package com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.testing.ViewModelScenario
import androidx.lifecycle.viewmodel.testing.viewModelScenario
import androidx.test.filters.SmallTest
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.SelectBottomSheetFragment
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.dsl.singleSelectGroup
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model.selectedItem
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

private fun <Id> createSelectVM(handleInit: SavedStateHandle.() -> Unit) = viewModelScenario {
    SelectBottomSheetViewModel<Id>(createSavedStateHandle().apply(handleInit))
}

private fun <Id, R> useSelectVM(
    handleInit: SavedStateHandle.() -> Unit = {},
    scenarioBlock: (ViewModelScenario<SelectBottomSheetViewModel<Id>>) -> R
) = viewModelScenario {
    SelectBottomSheetViewModel<Id>(createSavedStateHandle().apply(handleInit))
}.use(scenarioBlock)

@SmallTest
class SelectBottomSheetViewModelTest {
    @Test
    fun toggleSelectedItem_updateState() {
        val itemsData = singleSelectGroup {
            (1..10).forEach {
                addItem(it, "Item $it")
            }

            selectedIndex = 0
        }

        useSelectVM<Int, _>(
            handleInit = {
                this[SelectBottomSheetFragment.TAG_ITEMS_DATA] = itemsData
            }
        ) { scenario ->
            with(scenario.viewModel) {
                assertEquals(items.value.items, itemsData.items)

                val newSelected = itemsData.items.first { it.id == 5 }

                toggleSelectedItem(newSelected, true)

                assertNotNull(items.value.selectedItem) {
                    assertEquals(it, newSelected)
                }
            }
        }
    }
}
