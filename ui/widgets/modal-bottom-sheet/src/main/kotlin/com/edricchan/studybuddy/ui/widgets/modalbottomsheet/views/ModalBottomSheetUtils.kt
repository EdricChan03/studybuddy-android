package com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.dsl.ModalBottomSheetBuilder
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.dsl.items

/**
 * Instantiates a [modal bottom sheet][ModalBottomSheetFragment] given the specified options.
 * @param headerTitle The header title to use
 * @param items Items to be shown in the bottom sheet.
 */
inline fun modalBottomSheet(
    headerTitle: String? = null,
    hideDragHandle: Boolean = false,
    items: ModalBottomSheetBuilder.() -> Unit
): ModalBottomSheetFragment = ModalBottomSheetFragment.newInstance(
    items = items(items),
    headerTitle = headerTitle, hideDragHandle = hideDragHandle
)

/**
 * Instantiates a [modal bottom sheet][ModalBottomSheetFragment] given the specified options.
 * @param headerTitleRes The header title to use, as a string resource.
 * @param items Items to be shown in the bottom sheet.
 * @receiver The [Context] used to resolve the [headerTitleRes] resource.
 */
inline fun Context.modalBottomSheet(
    @StringRes headerTitleRes: Int,
    hideDragHandle: Boolean = false,
    items: ModalBottomSheetBuilder.() -> Unit
): ModalBottomSheetFragment = modalBottomSheet(
    headerTitle = getString(headerTitleRes),
    hideDragHandle = hideDragHandle,
    items = items
)

/** Shows a [modal bottom sheet][ModalBottomSheetFragment] given the specified options. */
inline fun FragmentManager.showModalBottomSheet(
    headerTitle: String? = null,
    hideDragHandle: Boolean = false,
    itemsInit: ModalBottomSheetBuilder.() -> Unit,
): ModalBottomSheetFragment = ModalBottomSheetFragment.newInstance(
    items(itemsInit), headerTitle, hideDragHandle
).apply {
    show(this@showModalBottomSheet, tag)
}

/**
 * Shows a [modal bottom sheet][ModalBottomSheetFragment] given the specified options.
 *
 * Note: The options specified ([headerTitle] and [items]) are set **after**
 * [ModalBottomSheetFragment.show] is called.
 * @param headerTitle The header title to use.
 * @param items Items to be shown in the bottom sheet.
 */
fun Fragment.showModalBottomSheet(
    headerTitle: String? = null,
    hideDragHandle: Boolean = false,
    itemsInit: ModalBottomSheetBuilder.() -> Unit
): ModalBottomSheetFragment = parentFragmentManager.showModalBottomSheet(
    headerTitle = headerTitle,
    hideDragHandle = hideDragHandle,
    itemsInit = itemsInit
)

/**
 * Shows a [modal bottom sheet][ModalBottomSheetFragment] given the specified options.
 * @param headerTitleRes The header title to use, as a string resource.
 * @param items Items to be shown in the bottom sheet.
 */
fun Fragment.showModalBottomSheet(
    @StringRes headerTitleRes: Int,
    hideDragHandle: Boolean = false,
    itemsInit: ModalBottomSheetBuilder.() -> Unit
): ModalBottomSheetFragment = showModalBottomSheet(
    headerTitle = getString(headerTitleRes),
    hideDragHandle = hideDragHandle,
    itemsInit = itemsInit
)

/**
 * Shows a [modal bottom sheet][ModalBottomSheetFragment] given the specified options.
 *
 * Note: The options specified ([headerTitle] and [items]) are set **after**
 * [ModalBottomSheetFragment.show] is called.
 * @param headerTitle The header title to use.
 * @param items Items to be shown in the bottom sheet.
 */
fun FragmentActivity.showModalBottomSheet(
    headerTitle: String? = null,
    hideDragHandle: Boolean = false,
    itemsInit: ModalBottomSheetBuilder.() -> Unit
): ModalBottomSheetFragment = supportFragmentManager.showModalBottomSheet(
    headerTitle = headerTitle,
    hideDragHandle = hideDragHandle,
    itemsInit = itemsInit
)

/**
 * Shows a [modal bottom sheet][ModalBottomSheetFragment] given the specified options.
 * @param headerTitleRes The header title to use, as a string resource.
 * @param items Items to be shown in the bottom sheet.
 */
fun FragmentActivity.showModalBottomSheet(
    @StringRes headerTitleRes: Int,
    hideDragHandle: Boolean = false,
    itemsInit: ModalBottomSheetBuilder.() -> Unit
): ModalBottomSheetFragment = showModalBottomSheet(
    headerTitle = getString(headerTitleRes),
    hideDragHandle = hideDragHandle,
    itemsInit = itemsInit
)
