package io.github.edricchan03.optionbottomsheet.models

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.shouldForAll
import io.kotest.matchers.collections.shouldBeSameSizeAs
import io.kotest.matchers.collections.shouldHaveAtMostSize
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.maps.shouldContainKeys
import io.kotest.matchers.maps.shouldHaveSize
import io.kotest.matchers.string.shouldStartWith
import io.kotest.property.Arb
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.set
import io.kotest.property.assume
import io.kotest.property.checkAll

class BottomSheetOptionGroupUnitTest : FunSpec({
    context("BottomSheetOptionGroup") {
        // Constructors
        context("require checks") {
            test("should throw IAE if the selected list is bigger than the items") {
                val items = listOf<BottomSheetOption>()
                val e = shouldThrow<IllegalArgumentException> {
                    BottomSheetOptionGroup(
                        items = items,
                        selectedIndices = Arb.set(Arb.int()).next()
                    )
                }
                e.message shouldStartWith "There should not be more selected indices of size"
            }
            test("should throw IAE if the selected indices are not in the items indices") {
                checkAll(
                    iterations = 100,
                    Arb.list(Arb.bottomSheetOption(), 2..100)
                ) { items ->
                    val selectedIndices = Arb.set(Arb.int(), items.indices)
                        .filter { indices -> indices.any { it !in items.indices } }
                        .bind()
                    assume {
                        with(selectedIndices) {
                            shouldNotBeEmpty()
                        }
                    }
                    val e = shouldThrow<IllegalArgumentException> {
                        BottomSheetOptionGroup(
                            items = items,
                            selectedIndices = selectedIndices
                        )
                    }
                    e.message shouldStartWith "The selected indices should only exist in the list " +
                        "of items' indices but selected indices"
                }
            }
        }
        context("Map constructor") {
            test("should set selection items correctly") {
                val selectionMap = mapOf(
                    BottomSheetOption(title = "Item 1") to false,
                    BottomSheetOption(title = "Item 2") to false,
                    BottomSheetOption(title = "Item 3") to true
                )

                val group = BottomSheetOptionGroup(
                    itemsSelectionMap = selectionMap
                )

                val expectedIndices = selectionMap.filterValues { it }
                group.selectedIndices shouldHaveSize expectedIndices.size
                group.selectedItems shouldHaveSize expectedIndices.size
            }
        }

        // Properties
        context("selectedItems") {
            test("should get the list of selected items") {
                checkAll(Arb.bottomSheetOptionGroup()) { group ->
                    assume {
                        group.selectedItems shouldBeSameSizeAs group.selectedIndices
                        group.selectedIndices shouldHaveAtMostSize group.items.size
                        group.selectedIndices.shouldForAll { it in group.items.indices }
                    }

                    group.selectedItems shouldBeSameSizeAs group.selectedIndices
                    group.selectedItems shouldBeEqual group.items.filterIndexed { index, _ ->
                        index in group.selectedIndices
                    }
                }
            }

            test("should return an empty list if no items are selected") {
                checkAll(Arb.list(Arb.bottomSheetOption())) {
                    val group = BottomSheetOptionGroup(
                        items = it,
                        selectedIndices = setOf()
                    )

                    group.selectedItems shouldHaveSize 0
                }
            }
        }
        context("itemSelectionMap") {
            test("should return the map of selected items") {
                checkAll(Arb.bottomSheetOptionGroup()) { group ->
                    assume(
                        group.selectedItems.size == group.selectedIndices.size &&
                            // We may have duplicate items that can't be handled by a Map,
                            // a data structure intended for non-duplicate keys
                            group.items.distinct().size == group.items.size
                    )

                    group.itemsSelectionMap shouldHaveSize group.items.size
                    group.itemsSelectionMap.shouldContainKeys(*group.items.toTypedArray())

                    val selectedEntries = group.itemsSelectionMap.filterValues { it }
                    selectedEntries.values shouldBeSameSizeAs group.selectedItems
                }
            }
            test("should return an empty map if there are no items") {
                val group = BottomSheetOptionGroup(items = listOf())
                group.itemsSelectionMap.shouldBeEmpty()
            }
        }
    }
})
