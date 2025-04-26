package com.edricchan.studybuddy.ui.modules.debug

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.view.plusAssign
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.MimeTypeConstants
import com.edricchan.studybuddy.databinding.FragDebugModalBottomSheetBinding
import com.edricchan.studybuddy.exts.android.showToast
import com.edricchan.studybuddy.exts.material.snackbar.showSnackbar
import com.edricchan.studybuddy.ui.common.fragment.ViewBindingFragment
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.dsl.showMultiSelectBottomSheet
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.dsl.showSingleSelectBottomSheet
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.ModalBottomSheetAdapter
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.interfaces.ModalBottomSheetItem
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.modalBottomSheet
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

// TODO: Move demo class to dedicated Gradle module
class DebugModalBottomSheetFragment :
    ViewBindingFragment<FragDebugModalBottomSheetBinding>(FragDebugModalBottomSheetBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.modalBottomSheetLauncherButtonsLayout) {
            addModalBottomSheetLauncher(
                "Modal bottom sheet with text items and no header",
                buildOnClickListener(modalBottomSheetWithTextNoHeader())
            )

            addModalBottomSheetLauncher(
                "Modal bottom sheet with text items and header",
                buildOnClickListener(modalBottomSheetWithTextAndHeader())
            )

            addModalBottomSheetLauncher(
                "Modal bottom sheet with icons and text items",
                buildOnClickListener(modalBottomSheetWithIconItems())
            )

            addModalBottomSheetLauncher(
                "Modal bottom sheet with 1000 text items",
                buildOnClickListener(modalBottomSheetWith1000Items())
            )

            addModalBottomSheetLauncher(
                "Modal bottom sheet with disabled even text items",
                buildOnClickListener(modalBottomSheetDisabledEvenItems())
            )

            addModalBottomSheetLauncher(
                "Modal bottom sheet with hidden odd text items",
                buildOnClickListener(modalBottomSheetHiddenOddItems())
            )

            addModalBottomSheetLauncher(
                "Modal bottom sheet with DSL syntax",
                buildOnClickListener(modalBottomSheetDSL())
            )

            addModalBottomSheetLauncher(
                "Modal bottom sheet without draggable handle",
                buildOnClickListener(modalBottomSheetNoDraggable())
            )

            // New select bottom sheet demos
            addModalBottomSheetLauncher(
                "Select bottom sheet (multi)"
            ) {
                showMultiSelectBottomSheet<Int>(
                    headerTitle = "Select something",
                    onCanceled = {
                        showSnackbar(
                            "Multi-select bottom sheet was cancelled",
                            Snackbar.LENGTH_SHORT
                        )
                    },
                    onConfirm = { items ->
                        showSnackbar(
                            "Items ${items.joinToString { it.id.toString() }} selected",
                            Snackbar.LENGTH_LONG,
                        )
                    }
                ) {
                    (0..10).forEach {
                        addItem(
                            id = it,
                            title = "Item $it"
                        ) {
                            enabled = it % 2 == 0
                        }
                    }
                    addItem(
                        id = 100,
                        title = "Item with icon",
                        selected = true
                    ) {
                        iconRes = R.drawable.ic_info_outline_24dp
                    }
                }
            }

            addModalBottomSheetLauncher(
                "Select bottom sheet (single)"
            ) {
                showSingleSelectBottomSheet<Int>(
                    headerTitle = "Select something",
                    onCanceled = {
                        showSnackbar(
                            "Single-select bottom sheet was cancelled",
                            Snackbar.LENGTH_SHORT
                        )
                    },
                    onConfirm = {
                        showSnackbar(
                            "Item ${it.id} selected",
                            Snackbar.LENGTH_LONG,
                        )
                    }
                ) {
                    (0..10).forEach {
                        addItem(
                            id = it,
                            title = "Item $it"
                        ) {
                            enabled = it % 2 == 0
                        }
                    }
                    addItem(
                        id = 100,
                        title = "Item with icon"
                    ) {
                        iconRes = R.drawable.ic_info_outline_24dp
                    }
                }
            }
        }
    }

    // Code adapted from
    // https://github.com/material-components/material-components-android/blob/master/catalog/java/io/material/catalog/picker/PickerMainDemoFragment.java
    private fun ViewGroup.addModalBottomSheetLauncher(
        @StringRes demoTitleResId: Int, onClickListener: View.OnClickListener
    ) {
        this += MaterialButton(
            context,
            null,
            com.google.android.material.R.attr.materialButtonOutlinedStyle
        ).apply {
            setOnClickListener(onClickListener)
            setText(demoTitleResId)
        }
    }

    private fun ViewGroup.addModalBottomSheetLauncher(
        demoTitle: String, onClickListener: View.OnClickListener
    ) {
        this += MaterialButton(
            context,
            null,
            com.google.android.material.R.attr.materialButtonOutlinedStyle
        ).apply {
            setOnClickListener(onClickListener)
            text = demoTitle
        }
    }

    // Code adapted from
    // https://github.com/material-components/material-components-android/blob/master/catalog/java/io/material/catalog/picker/PickerMainDemoFragment.java
    private fun buildOnClickListener(bottomSheetDialogFragment: BottomSheetDialogFragment): View.OnClickListener {
        return View.OnClickListener {
            bottomSheetDialogFragment.show(
                parentFragmentManager,
                bottomSheetDialogFragment.tag
            )
        }
    }

    private fun showToast(item: ModalBottomSheetItem) {
        showToast("Item ${item.id} clicked!", Toast.LENGTH_SHORT)
    }

    private fun showSnackbar(text: String, @BaseTransientBottomBar.Duration duration: Int) {
        showSnackbar(binding.root, text, duration)
    }

    private val onItemClickListener = ModalBottomSheetAdapter.OnItemClickListener { showToast(it) }
    private val onItemCheckedChangeListener =
        ModalBottomSheetAdapter.OnItemCheckedChangeListener { showToast(it) }

    private fun modalBottomSheetWithTextNoHeader() =
        modalBottomSheet {
            for (i in 1..10) {
                item(
                    ModalBottomSheetItem(
                        id = i, title = "Item $i",
                        onItemClickListener = onItemClickListener
                    )
                )
            }
        }

    private fun modalBottomSheetWithTextAndHeader() =
        requireContext().modalBottomSheet(
            headerTitleRes = R.string.share_intent_value
        ) {
            val packageManager = requireContext().packageManager
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                putExtra(
                    Intent.EXTRA_TEXT,
                    this@DebugModalBottomSheetFragment.getString(R.string.share_content)
                )
                type = MimeTypeConstants.textPlainMime
            }
            // See https://stackoverflow.com/a/9083910/6782707
            val launchables =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) packageManager.queryIntentActivities(
                    shareIntent,
                    PackageManager.ResolveInfoFlags.of(0)
                ) else packageManager.queryIntentActivities(shareIntent, 0)
            items(
                launchables.sortedWith(ResolveInfo.DisplayNameComparator(packageManager))
                    .map { resolveInfo ->
                        ModalBottomSheetItem(
                            title = resolveInfo.loadLabel(packageManager).toString(),
                            icon = resolveInfo.loadIcon(packageManager),
                            onItemClickListener = {
                                showToast(it)
                                // Code adapted from
                                // https://github.com/commonsguy/cw-advandroid/blob/master/Introspection/Launchalot/src/com/commonsware/android/launchalot/Launchalot.java
                                val activity = resolveInfo.activityInfo
                                // Passing an intent to the `Intent` constructor will create a copy of
                                // that intent.
                                val intent = Intent(shareIntent).apply {
                                    component = ComponentName(activity.packageName, activity.name)
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                }
                                startActivity(intent)
                            }
                        )
                    }
            )

        }

    private fun modalBottomSheetWith1000Items() =
        modalBottomSheet {
            for (i in 1..1000) {
                item(
                    ModalBottomSheetItem(
                        id = i,
                        title = "Item $i",
                        onItemClickListener = onItemClickListener
                    )
                )
            }
        }

    private fun modalBottomSheetWithIconItems() =
        modalBottomSheet {
            items(
                ModalBottomSheetItem(
                    id = 1, title = "About app", icon = R.drawable.ic_info_outline_24dp,
                    onItemClickListener = onItemClickListener
                ),
                ModalBottomSheetItem(
                    id = 2, title = "Share this app", icon = R.drawable.ic_share_outline_24dp,
                    onItemClickListener = onItemClickListener
                ),
                ModalBottomSheetItem(
                    id = 3, title = "Account", icon = R.drawable.ic_account_circle_outline_24dp,
                    onItemClickListener = onItemClickListener
                ),
                ModalBottomSheetItem(
                    id = 4, title = "Settings", icon = R.drawable.ic_settings_outline_24dp,
                    onItemClickListener = onItemClickListener
                ),
                ModalBottomSheetItem(
                    id = 5, title = "Help", icon = R.drawable.ic_help_outline_24dp,
                    onItemClickListener = onItemClickListener
                )
            )
        }

    private fun modalBottomSheetDisabledEvenItems() =
        modalBottomSheet {
            for (i in 1..10) {
                // Only disable on even items
                val disabled = i % 2 == 0
                item(
                    ModalBottomSheetItem(
                        id = i,
                        title = "Item $i",
                        enabled = !disabled,
                        onItemClickListener = onItemClickListener
                    )
                )
            }
        }

    private fun modalBottomSheetHiddenOddItems() =
        modalBottomSheet {
            for (i in 1..10) {
                // Only hide on odd items
                // This indicates that all even items are visible
                val visible = i % 2 == 0
                item(
                    ModalBottomSheetItem(
                        id = i,
                        title = "Item $i",
                        visible = visible,
                        onItemClickListener = onItemClickListener
                    )
                )
            }
        }

    private fun modalBottomSheetDSL() =
        modalBottomSheet {
            for (i in 1..10) {
                item(title = "Item $i") {
                    id = i
                    onItemClickListener = this@DebugModalBottomSheetFragment.onItemClickListener
                }
            }
            // plusAssign test
            this += item(title = "Item from plusAssign operator")
            this += {
                title = "Item from plusAssign with DSL"
            }
        }

    private fun modalBottomSheetNoDraggable() = modalBottomSheet(
        hideDragHandle = true
    ) {
        for (item in 1..5) {
            item("Item $item") {
                id = item
                onItemClickListener = this@DebugModalBottomSheetFragment.onItemClickListener
            }
        }
    }
}
