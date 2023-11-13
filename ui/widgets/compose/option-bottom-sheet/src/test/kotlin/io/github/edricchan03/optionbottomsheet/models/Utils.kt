package io.github.edricchan03.optionbottomsheet.models

import androidx.compose.runtime.Composable
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.enum
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.set
import io.kotest.property.arbitrary.string

// TODO: Move to test-fixtures once AGP supports Kotlin source sets for test-fixtures - see
//  - https://issuetracker.google.com/issues/139438142
//  - https://issuetracker.google.com/issues/259523353

fun Arb.Companion.bottomSheetOption(
    idArb: Arb<Int> = Arb.int(),
    titleArb: Arb<String> = Arb.string(),
    icon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit = {},
    visibleArb: Arb<Boolean> = Arb.boolean(),
    enabledArb: Arb<Boolean> = Arb.boolean()
) = arbitrary {
    BottomSheetOption(
        id = idArb.bind(),
        title = titleArb.bind(),
        icon = icon,
        onClick = onClick,
        visible = visibleArb.bind(),
        enabled = enabledArb.bind()
    )
}

val DefaultSelectedIndicesArb: (List<BottomSheetOption>) -> Arb<Set<Int>> = { items ->
    // There's a check to verify that the indices range is non-empty, so we need to add this
    // as an edge-case
    if (items.isEmpty()) Arb.set(Arb.int(), size = 0)
    else Arb.set(Arb.int(items.indices), items.indices)
}

fun Arb.Companion.bottomSheetOptionGroup(
    titleArb: Arb<String?> = Arb.string(),
    itemsArb: Arb<List<BottomSheetOption>> = Arb.list(Arb.bottomSheetOption()),
    visibleArb: Arb<Boolean> = Arb.boolean(),
    enabledArb: Arb<Boolean> = Arb.boolean(),
    onCheckedChange: BottomSheetOptionGroupCheckedChangeListener = { _, _ -> },
    checkableBehaviorArb: Arb<BottomSheetOptionGroup.CheckableBehavior> =
        Arb.enum<BottomSheetOptionGroup.CheckableBehavior>(),
    selectedIndicesArb: (List<BottomSheetOption>) -> Arb<Set<Int>> = DefaultSelectedIndicesArb
) = arbitrary {
    val items = itemsArb.bind()
    BottomSheetOptionGroup(
        title = titleArb.bind(),
        items = items,
        visible = visibleArb.bind(),
        enabled = enabledArb.bind(),
        onCheckedChange = onCheckedChange,
        checkableBehavior = checkableBehaviorArb.bind(),
        selectedIndices = selectedIndicesArb(items).bind()
    )
}

fun Arb.Companion.bottomSheetOptionGroupMap(
    titleArb: Arb<String?> = Arb.string(),
    itemsSelectionMapArb: Arb<Map<BottomSheetOption, Boolean>> = Arb.map(
        Arb.bottomSheetOption(),
        Arb.boolean()
    ),
    visibleArb: Arb<Boolean> = Arb.boolean(),
    enabledArb: Arb<Boolean> = Arb.boolean(),
    onCheckedChange: BottomSheetOptionGroupCheckedChangeListener = { _, _ -> },
    checkableBehaviorArb: Arb<BottomSheetOptionGroup.CheckableBehavior> =
        Arb.enum<BottomSheetOptionGroup.CheckableBehavior>(),
) = arbitrary {
    BottomSheetOptionGroup(
        title = titleArb.bind(),
        itemsSelectionMap = itemsSelectionMapArb.bind(),
        visible = visibleArb.bind(),
        enabled = enabledArb.bind(),
        onCheckedChange = onCheckedChange,
        checkableBehavior = checkableBehaviorArb.bind()
    )
}
