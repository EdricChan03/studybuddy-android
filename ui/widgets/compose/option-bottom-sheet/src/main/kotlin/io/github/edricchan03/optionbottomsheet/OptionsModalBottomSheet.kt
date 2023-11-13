package io.github.edricchan03.optionbottomsheet

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.edricchan03.optionbottomsheet.models.BottomSheetOption
import io.github.edricchan03.optionbottomsheet.models.BottomSheetOptionGroup
import io.github.edricchan03.optionbottomsheet.models.dsl.BottomSheetOptionGroupBuilder
import io.github.edricchan03.optionbottomsheet.models.dsl.optionGroup

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionsModalBottomSheet(
    modifier: Modifier = Modifier,
    sheetModifier: Modifier = Modifier,
    group: BottomSheetOptionGroup,
    onDismissRequest: () -> Unit = {},
    state: SheetState = rememberModalBottomSheetState()
) = ModalBottomSheet(
    modifier = sheetModifier,
    onDismissRequest = onDismissRequest,
    sheetState = state
) {
    OptionsBottomSheetContent(
        modifier = modifier,
        group = group
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionsModalBottomSheet(
    modifier: Modifier = Modifier,
    sheetModifier: Modifier = Modifier,
    title: String? = null,
    items: List<BottomSheetOption>,
    onDismissRequest: () -> Unit = {},
    state: SheetState = rememberModalBottomSheetState()
) = ModalBottomSheet(
    modifier = sheetModifier,
    onDismissRequest = onDismissRequest,
    sheetState = state
) {
    OptionsBottomSheetContent(
        modifier = modifier,
        title = title,
        items = items
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionsModalBottomSheet(
    modifier: Modifier = Modifier,
    sheetModifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    state: SheetState = rememberModalBottomSheetState(),
    group: @Composable BottomSheetOptionGroupBuilder.() -> Unit
) = OptionsModalBottomSheet(
    modifier = modifier,
    sheetModifier = sheetModifier,
    group = optionGroup(group),
    onDismissRequest = onDismissRequest,
    state = state
)
