package io.github.edricchan03.optionbottomsheet

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.edricchan03.optionbottomsheet.models.BottomSheetOption
import io.github.edricchan03.optionbottomsheet.models.BottomSheetOptionGroup

/**
 * The content of [OptionsModalBottomSheet].
 * @param onDismissBottomSheetRequest Lambda that is invoked to indicate a request to dismiss
 * the parent [OptionsModalBottomSheet]. The [BottomSheetOption] that was clicked on is passed
 * as the `item` parameter.
 * @param group The option group to display.
 */
@Deprecated(
    "Use the overload which takes a header composable instead",
    ReplaceWith(
        "OptionsBottomSheetContent(modifier = modifier, " +
            "onDismissBottomSheetRequest = onDismissBottomSheetRequest, group = group)",
        "io.github.edricchan03.optionbottomsheet.OptionsBottomSheetContent"
    )
)
@Composable
fun OptionsBottomSheetContent(
    modifier: Modifier = Modifier,
    headerModifier: Modifier,
    onDismissBottomSheetRequest: (item: BottomSheetOption) -> Unit,
    group: BottomSheetOptionGroup
) {
    group.title?.let { OptionsBottomSheetDefaults.Header(modifier = headerModifier, title = it) }
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
@Deprecated(
    "Use the overload which takes a header composable instead",
    ReplaceWith(
        "OptionsBottomSheetContent(modifier = modifier, " +
            "onDismissBottomSheetRequest = onDismissBottomSheetRequest, title = title, " +
            "items = items)",
        "io.github.edricchan03.optionbottomsheet.OptionsBottomSheetContent"
    )
)
@Composable
fun OptionsBottomSheetContent(
    modifier: Modifier = Modifier,
    headerModifier: Modifier,
    onDismissBottomSheetRequest: (item: BottomSheetOption) -> Unit,
    title: String? = null,
    items: List<BottomSheetOption>
) = OptionsBottomSheetContent(
    modifier = modifier,
    headerModifier = headerModifier,
    group = BottomSheetOptionGroup(title = title, items = items),
    onDismissBottomSheetRequest = onDismissBottomSheetRequest
)

/**
 * The content of [OptionsModalBottomSheet].
 * @param group The [BottomSheetOptionGroup] to display.
 * @param header The header composable to display.
 * Defaults to [OptionsBottomSheetDefaults.Header] with the value of
 * [BottomSheetOptionGroup.title] if set.
 * @param onDismissBottomSheetRequest Lambda that is invoked to indicate a request to dismiss
 * the parent [OptionsModalBottomSheet]. The [BottomSheetOption] that was clicked on is passed
 * as the `item` parameter.
 */
@Composable
fun OptionsBottomSheetContent(
    modifier: Modifier = Modifier,
    group: BottomSheetOptionGroup,
    header: @Composable (() -> Unit)? = group.title?.let {
        { OptionsBottomSheetDefaults.Header(title = it) }
    },
    onDismissBottomSheetRequest: (item: BottomSheetOption) -> Unit,
) {
    header?.invoke()
    OptionsBottomSheetList(
        modifier = modifier,
        group = group,
        onDismissBottomSheetRequest = onDismissBottomSheetRequest
    )
}

/**
 * The content of [OptionsModalBottomSheet].
 * @param title The header title.
 * @param header The header composable to use. Defaults to [OptionsBottomSheetDefaults.Header]
 * if [title] is set.
 * @param items The list of options to display.
 * @param onDismissBottomSheetRequest Lambda that is invoked to indicate a request to dismiss
 * the parent [OptionsModalBottomSheet]. The [BottomSheetOption] that was clicked on is passed
 * as the `item` parameter.
 */
@Composable
fun OptionsBottomSheetContent(
    modifier: Modifier = Modifier,
    title: String? = null,
    header: @Composable (() -> Unit)? = title?.let {
        { OptionsBottomSheetDefaults.Header(title = it) }
    },
    items: List<BottomSheetOption>,
    onDismissBottomSheetRequest: (item: BottomSheetOption) -> Unit,
) = OptionsBottomSheetContent(
    modifier = modifier,
    group = BottomSheetOptionGroup(title = title, items = items),
    header = header,
    onDismissBottomSheetRequest = onDismissBottomSheetRequest
)
