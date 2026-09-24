package com.edricchan.studybuddy.ui.widgets.compose.form.chips.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.annotation.FrequentlyChangingValue
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList

class ChipTextFieldState<C : ChipItem>(
    initialChips: Set<C> = setOf(),
    private val chipFactory: ChipItem.Factory<C>
) {
    val chipsState: SnapshotStateList<C> = initialChips.toMutableStateList()

    fun onSubmit(item: C) {
        if (item !in chipsState) chipsState += item
    }

    fun onSubmit(text: String) {
        val item = chipFactory.create(text)
        if (item !in chipsState) chipsState += item
    }

    fun onRemoveLast() {
        if (chipsState.isEmpty()) return
        chipsState.removeAt(chipsState.lastIndex)
    }

    fun onRemoveChip(item: C) {
        chipsState -= item
    }

    @get:FrequentlyChangingValue
    val chips: Set<C> get() = chipsState.toSet()

    companion object {
        fun <C : ChipItem> Saver(
            chipFactory: ChipItem.Factory<C>
        ): Saver<ChipTextFieldState<C>, Any> = listSaver(
            save = {
                listOf(
                    it.chipsState.toSet()
                )
            },
            restore = {
                ChipTextFieldState(
                    initialChips = it[0] as Set<C>,
                    chipFactory = chipFactory
                )
            }
        )
    }
}

@Composable
fun <C : ChipItem> rememberChipTextFieldState(
    initialChips: Set<C> = setOf(),
    chipFactory: ChipItem.Factory<C>
): ChipTextFieldState<C> = rememberSaveable(
    initialChips,
    chipFactory,
    saver = ChipTextFieldState.Saver(chipFactory = chipFactory)
) {
    ChipTextFieldState(
        initialChips = initialChips,
        chipFactory = chipFactory
    )
}

@Composable
fun rememberChipTextFieldState(
    initialChips: Set<DefaultChipItem> = setOf()
): ChipTextFieldState<DefaultChipItem> = rememberSaveable(
    initialChips,
    saver = ChipTextFieldState.Saver(chipFactory = DefaultChipItem.Factory)
) {
    ChipTextFieldState(
        initialChips = initialChips,
        chipFactory = DefaultChipItem.Factory
    )
}
