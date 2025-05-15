package com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.property.Arb
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.arbitrary.set
import io.kotest.property.checkAll

class MultiSelectOptionBottomSheetGroupTest : DescribeSpec({
    describe("isChecked") {
        it("should return true when at least one item is selected") {
            checkAll(
                Arb.multiSelectGroup(
                    itemsArb = Arb.map(
                        Arb.optionBottomSheetItem(
                            Arb.int()
                        ), Arb.boolean()
                    )
                )
            ) { group ->
                if (group.isEmpty()) {
                    group.isChecked(Arb.optionBottomSheetItem(Arb.int()).bind()) shouldBeEqual false
                    return@checkAll
                }
                val selectedItems =
                    group.shuffled(randomSource().random).take(Arb.positiveInt().bind()).toSet()

                val copied = group.copy(
                    selectedItems = selectedItems
                )

                selectedItems.forAll {
                    copied.isChecked(it) shouldBeEqual true
                }
                copied.selectedItems shouldBeEqual selectedItems
            }
        }

        it("should return false when no items are selected") {
            checkAll(
                Arb.multiSelectGroup(
                    itemsArb = Arb.list(
                        Arb.optionBottomSheetItem(
                            Arb.int()
                        )
                    ),
                    selectedIndicesArb = Arb.set(Arb.int(), 0)
                )
            ) { group ->
                if (group.isEmpty()) {
                    group.isChecked(Arb.optionBottomSheetItem(Arb.int()).bind()) shouldBeEqual false
                    return@checkAll
                }

                group.forAll {
                    group.isChecked(it) shouldBeEqual false
                }
            }
        }
    }

    describe("hasSelection") {
        it("should return true when items are selected") {
            checkAll(
                Arb.multiSelectGroup(
                    itemsArb = Arb.map(
                        Arb.optionBottomSheetItem(
                            Arb.int()
                        ),
                        Arb.boolean()
                    )
                )
            ) {
                it.hasSelection shouldBeEqual it.selectedItems.isNotEmpty()
            }
        }
    }

    describe("selectedItems") {
        it("should return the set of selected items") {
            checkAll(
                Arb.multiSelectGroup(
                    itemsArb = Arb.map(
                        Arb.optionBottomSheetItem(
                            Arb.int()
                        ),
                        Arb.boolean()
                    )
                )
            ) {
                it.selectedItems shouldBeEqual it.items
                    .filterIndexed { index, _ -> index in it.selectedIndices }
                    .toSet()
            }
        }

        it("should return the set of valid selected items") {
            checkAll(
                Arb.multiSelectGroup(
                    itemsArb = Arb.list(
                        Arb.optionBottomSheetItem(
                            Arb.int()
                        )
                    )
                )
            ) {
                it.selectedItems shouldBeEqual it.items
                    .filterIndexed { index, _ -> index in it.selectedIndices }
                    .toSet()
            }
        }
    }
})
