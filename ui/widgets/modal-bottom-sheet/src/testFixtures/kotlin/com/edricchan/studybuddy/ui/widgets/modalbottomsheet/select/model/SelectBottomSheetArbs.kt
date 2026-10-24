package com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.orNull
import io.kotest.property.arbitrary.set
import io.kotest.property.arbitrary.string

fun Arb.Companion.optionBottomSheetItem(
    idArb: Arb<String>,
    titleArb: Arb<String> = Arb.string(),
    iconArb: Arb<Int?> = Arb.int().orNull(),
    enabledArb: Arb<Boolean> = Arb.boolean()
): Arb<OptionBottomSheetItem> = arbitrary {
    OptionBottomSheetItem(
        id = idArb.bind(),
        title = titleArb.bind(),
        icon = iconArb.bind(),
        enabled = enabledArb.bind()
    )
}

fun Arb.Companion.multiSelectGroup(
    itemsArb: Arb<Map<OptionBottomSheetItem, Boolean>>,
    enabledArb: Arb<Boolean> = Arb.boolean()
): Arb<OptionBottomSheetGroup.MultiSelect> = arbitrary {
    OptionBottomSheetGroup.MultiSelect(
        itemsSelectionMap = itemsArb.bind(),
        enabled = enabledArb.bind()
    )
}

fun Arb.Companion.multiSelectGroup(
    itemsArb: Arb<List<OptionBottomSheetItem>>,
    selectedIndicesArb: Arb<Set<Int>> = Arb.set(Arb.int()),
    enabledArb: Arb<Boolean> = Arb.boolean()
): Arb<OptionBottomSheetGroup.MultiSelect> = arbitrary {
    OptionBottomSheetGroup.MultiSelect(
        items = itemsArb.bind(),
        selectedIndices = selectedIndicesArb.bind(),
        enabled = enabledArb.bind()
    )
}

fun Arb.Companion.singleSelectGroup(
    itemsArb: Arb<List<OptionBottomSheetItem>>,
    enabledArb: Arb<Boolean> = Arb.boolean(),
    selectedIndexArb: Arb<Int?> = Arb.int().orNull()
): Arb<OptionBottomSheetGroup.SingleSelect> = arbitrary {
    OptionBottomSheetGroup.SingleSelect(
        items = itemsArb.bind(),
        enabled = enabledArb.bind(),
        selectedIndex = selectedIndexArb.bind()
    )
}

fun Arb.Companion.singleSelectGroup(
    itemIdArb: Arb<String>,
    itemsArb: Arb<List<OptionBottomSheetItem>> = Arb.list(Arb.optionBottomSheetItem(idArb = itemIdArb)),
    enabledArb: Arb<Boolean> = Arb.boolean(),
    selectedIndexArb: Arb<Int?> = Arb.int().orNull()
): Arb<OptionBottomSheetGroup.SingleSelect> = arbitrary {
    OptionBottomSheetGroup.SingleSelect(
        items = itemsArb.bind(),
        enabled = enabledArb.bind(),
        selectedIndex = selectedIndexArb.bind()
    )
}
