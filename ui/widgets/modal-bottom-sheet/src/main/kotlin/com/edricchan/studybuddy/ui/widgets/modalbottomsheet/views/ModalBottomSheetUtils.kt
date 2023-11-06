package com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.dsl.ModalBottomSheetBuilder

/** Instantiates a [modal bottom sheet][ModalBottomSheetFragment] given the specified options. */
inline fun modalBottomSheet(init: ModalBottomSheetFragment.() -> Unit) =
    ModalBottomSheetFragment().apply(init)

/**
 * Instantiates a [modal bottom sheet][ModalBottomSheetFragment] given the specified options.
 * @param headerTitle The header title to use
 * @param items Items to be shown in the bottom sheet.
 */
fun modalBottomSheet(
    headerTitle: String,
    items: ModalBottomSheetBuilder.() -> Unit
) = modalBottomSheet {
    this.headerTitle = headerTitle
    this.setItems(items)
}

/**
 * Instantiates a [modal bottom sheet][ModalBottomSheetFragment] given the specified options.
 * @param headerTitleRes The header title to use, as a string resource.
 * @param items Items to be shown in the bottom sheet.
 * @receiver The [Context] used to resolve the [headerTitleRes] resource.
 */
fun Context.modalBottomSheet(
    @StringRes headerTitleRes: Int,
    items: ModalBottomSheetBuilder.() -> Unit
) = modalBottomSheet(getString(headerTitleRes), items)

/** Shows a [modal bottom sheet][ModalBottomSheetFragment] given the specified options. */
inline fun FragmentManager.showModalBottomSheet(
    init: ModalBottomSheetFragment.() -> Unit
) {
    val frag = modalBottomSheet(init)
    frag.show(this, frag.tag)
}

/**
 * Shows a [modal bottom sheet][ModalBottomSheetFragment] given the specified
 * options.
 */
inline fun Fragment.showModalBottomSheet(
    init: ModalBottomSheetFragment.() -> Unit
) = parentFragmentManager.showModalBottomSheet(init)

/**
 * Shows a [modal bottom sheet][ModalBottomSheetFragment] given the specified options.
 * @param headerTitle The header title to use.
 * @param items Items to be shown in the bottom sheet.
 */
fun Fragment.showModalBottomSheet(
    headerTitle: String,
    items: ModalBottomSheetBuilder.() -> Unit
) = showModalBottomSheet {
    this.headerTitle = headerTitle
    this.setItems(items)
}

/**
 * Shows a [modal bottom sheet][ModalBottomSheetFragment] given the specified options.
 * @param headerTitleRes The header title to use, as a string resource.
 * @param items Items to be shown in the bottom sheet.
 */
fun Fragment.showModalBottomSheet(
    @StringRes headerTitleRes: Int,
    items: ModalBottomSheetBuilder.() -> Unit
) = showModalBottomSheet(getString(headerTitleRes), items)

/**
 * Shows a [modal bottom sheet][ModalBottomSheetFragment] given the specified
 * options.
 */
inline fun FragmentActivity.showModalBottomSheet(
    init: ModalBottomSheetFragment.() -> Unit
) = supportFragmentManager.showModalBottomSheet(init)

/**
 * Shows a [modal bottom sheet][ModalBottomSheetFragment] given the specified options.
 * @param headerTitle The header title to use.
 * @param items Items to be shown in the bottom sheet.
 */
fun FragmentActivity.showModalBottomSheet(
    headerTitle: String,
    items: ModalBottomSheetBuilder.() -> Unit
) = showModalBottomSheet {
    this.headerTitle = headerTitle
    this.setItems(items)
}

/**
 * Shows a [modal bottom sheet][ModalBottomSheetFragment] given the specified options.
 * @param headerTitleRes The header title to use, as a string resource.
 * @param items Items to be shown in the bottom sheet.
 */
fun FragmentActivity.showModalBottomSheet(
    @StringRes headerTitleRes: Int,
    items: ModalBottomSheetBuilder.() -> Unit
) = showModalBottomSheet(getString(headerTitleRes), items)
