package io.github.edricchan03.optionbottomsheet

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.edricchan03.optionbottomsheet.models.BottomSheetOption
import io.github.edricchan03.optionbottomsheet.models.BottomSheetOptionGroup

/**
 * The content of [OptionsModalBottomSheet].
 * @param onDismissBottomSheetRequest Lambda that is invoked to indicate a request to dismiss
 * the parent [OptionsModalBottomSheet]. The [BottomSheetOption] that was clicked on is passed
 * as the `item` parameter.
 * @param group The option group to display.
 */
@Composable
fun OptionsBottomSheetContent(
    modifier: Modifier = Modifier,
    headerModifier: Modifier = Modifier,
    onDismissBottomSheetRequest: (item: BottomSheetOption) -> Unit,
    group: BottomSheetOptionGroup
) {
    OptionsBottomSheetHeader(modifier = headerModifier, title = group.title)
    OptionsBottomSheetList(
        modifier = modifier,
        group = group,
        onDismissBottomSheetRequest = onDismissBottomSheetRequest
    )
}

/**
 * The content of [OptionsModalBottomSheet].
 * @param onDismissBottomSheetRequest Lambda that is invoked to indicate a request to dismiss
 * the parent [OptionsModalBottomSheet]. The [BottomSheetOption] that was clicked on is passed
 * as the `item` parameter.
 * @param title The header title.
 * @param items The list of options to display.
 */
@Composable
fun OptionsBottomSheetContent(
    modifier: Modifier = Modifier,
    headerModifier: Modifier = Modifier,
    onDismissBottomSheetRequest: (item: BottomSheetOption) -> Unit,
    title: String? = null,
    items: List<BottomSheetOption>
) = OptionsBottomSheetContent(
    modifier = modifier,
    headerModifier = headerModifier,
    group = BottomSheetOptionGroup(title = title, items = items),
    onDismissBottomSheetRequest = onDismissBottomSheetRequest
)

@Composable
internal fun OptionsBottomSheetHeader(
    modifier: Modifier = Modifier,
    title: String?
) {
    if (title == null) return

    Text(
        modifier = modifier.padding(start = 20.dp, top = 16.dp, end = 16.dp, bottom = 8.dp),
        text = title,
        style = MaterialTheme.typography.titleMedium
    )
}
