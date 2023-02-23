package com.edricchan.studybuddy.ui.modules.debug

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.view.plusAssign
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.MimeTypeConstants
import com.edricchan.studybuddy.databinding.ActivityDebugModalBottomSheetBinding
import com.edricchan.studybuddy.extensions.showToast
import com.edricchan.studybuddy.ui.common.BaseActivity
import com.edricchan.studybuddy.ui.widget.bottomsheet.ModalBottomSheetAdapter
import com.edricchan.studybuddy.ui.widget.bottomsheet.ModalBottomSheetFragment
import com.edricchan.studybuddy.ui.widget.bottomsheet.interfaces.ModalBottomSheetGroup
import com.edricchan.studybuddy.ui.widget.bottomsheet.interfaces.ModalBottomSheetItem
import com.edricchan.studybuddy.ui.widget.bottomsheet.modalBottomSheet
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton

// TODO: Move demo class to dedicated Gradle module
class DebugModalBottomSheetActivity : BaseActivity() {
    private lateinit var binding: ActivityDebugModalBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDebugModalBottomSheetBinding.inflate(layoutInflater)
            .also { setContentView(it.root) }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        with(binding.modalBottomSheetLauncherButtonsLayout) {
            addModalBottomSheetLauncher(
                this,
                "Modal bottom sheet with text items and no header",
                buildOnClickListener(modalBottomSheetWithTextNoHeader())
            )

            addModalBottomSheetLauncher(
                this,
                "Modal bottom sheet with text items and header",
                buildOnClickListener(modalBottomSheetWithTextAndHeader())
            )

            addModalBottomSheetLauncher(
                this,
                "Modal bottom sheet with icons and text items",
                buildOnClickListener(modalBottomSheetWithIconItems())
            )

            addModalBottomSheetLauncher(
                this,
                "Modal bottom sheet with 1000 text items",
                buildOnClickListener(modalBottomSheetWith1000Items())
            )

            addModalBottomSheetLauncher(
                this,
                "Modal bottom sheet with text items with checkboxes",
                buildOnClickListener(modalBottomSheetCheckBoxItems())
            )

            addModalBottomSheetLauncher(
                this,
                "Modal bottom sheet with text items with radio buttons",
                buildOnClickListener(modalBottomSheetRadioButtonItems())
            )

            addModalBottomSheetLauncher(
                this,
                "Modal bottom sheet with disabled even text items",
                buildOnClickListener(modalBottomSheetDisabledEvenItems())
            )

            addModalBottomSheetLauncher(
                this,
                "Modal bottom sheet with hidden odd text items",
                buildOnClickListener(modalBottomSheetHiddenOddItems())
            )

            addModalBottomSheetLauncher(
                this,
                "Modal bottom sheet with DSL syntax",
                buildOnClickListener(modalBottomSheetDSL())
            )

            addModalBottomSheetLauncher(
                this,
                "Modal bottom sheet without draggable handle",
                buildOnClickListener(modalBottomSheetNoDraggable())
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            true
        } else super.onOptionsItemSelected(item)
    }

    // Code adapted from
    // https://github.com/material-components/material-components-android/blob/master/catalog/java/io/material/catalog/picker/PickerMainDemoFragment.java
    private fun addModalBottomSheetLauncher(
        viewGroup: ViewGroup, @StringRes stringResId: Int, onClickListener: View.OnClickListener
    ) {
        viewGroup += MaterialButton(viewGroup.context).apply {
            setOnClickListener(onClickListener)
            setText(stringResId)
        }
    }

    private fun addModalBottomSheetLauncher(
        viewGroup: ViewGroup, string: String, onClickListener: View.OnClickListener
    ) {
        viewGroup += MaterialButton(viewGroup.context).apply {
            setOnClickListener(onClickListener)
            text = string
        }
    }

    // Code adapted from
    // https://github.com/material-components/material-components-android/blob/master/catalog/java/io/material/catalog/picker/PickerMainDemoFragment.java
    private fun buildOnClickListener(bottomSheetDialogFragment: BottomSheetDialogFragment): View.OnClickListener {
        return View.OnClickListener {
            bottomSheetDialogFragment.show(
                supportFragmentManager,
                bottomSheetDialogFragment.tag
            )
        }
    }

    private fun showToast(item: ModalBottomSheetItem) {
        showToast("Item ${item.id} clicked!", Toast.LENGTH_SHORT)
    }

    private val onItemClickListener = ModalBottomSheetAdapter.OnItemClickListener { showToast(it) }
    private val onItemCheckedChangeListener =
        ModalBottomSheetAdapter.OnItemCheckedChangeListener { showToast(it) }

    private fun modalBottomSheetWithTextNoHeader() =
        ModalBottomSheetFragment().apply {
            for (i in 1..10) {
                addItem(
                    ModalBottomSheetItem(
                        id = i, title = "Item $i",
                        onItemClickListener = onItemClickListener
                    )
                )
            }
        }

    private fun modalBottomSheetWithTextAndHeader() =
        ModalBottomSheetFragment().apply {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                putExtra(
                    Intent.EXTRA_TEXT,
                    this@DebugModalBottomSheetActivity.getString(R.string.share_content)
                )
                type = MimeTypeConstants.textPlainMime
            }
            // See https://stackoverflow.com/a/9083910/6782707
            @Suppress("DEPRECATION") val launchables =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) packageManager.queryIntentActivities(
                    shareIntent,
                    PackageManager.ResolveInfoFlags.of(0)
                ) else packageManager.queryIntentActivities(shareIntent, 0)
            items += launchables.sortedWith(ResolveInfo.DisplayNameComparator(packageManager))
                .map { resolveInfo ->
                    ModalBottomSheetItem(
                        title = resolveInfo.loadLabel(packageManager).toString(),
                        iconDrawable = resolveInfo.loadIcon(packageManager),
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

                            // Dismiss the bottom sheet
                            dismiss()
                        }
                    )
                }
            headerTitle = this@DebugModalBottomSheetActivity.getString(R.string.share_intent_value)
        }

    private fun modalBottomSheetWith1000Items() =
        ModalBottomSheetFragment().apply {
            for (i in 1..1000) {
                addItem(
                    ModalBottomSheetItem(
                        id = i,
                        title = "Item $i",
                        onItemClickListener = onItemClickListener
                    )
                )
            }
        }

    private fun modalBottomSheetWithIconItems() =
        ModalBottomSheetFragment().apply {
            items = mutableListOf(
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

    private fun modalBottomSheetCheckBoxItems() =
        ModalBottomSheetFragment().apply {
            val group = ModalBottomSheetGroup(
                id = 1000,
                checkableBehavior = ModalBottomSheetGroup.CHECKABLE_BEHAVIOR_ALL,
                onItemCheckedChangeListener = onItemCheckedChangeListener
            )
            for (i in 1..10) {
                addItem(
                    ModalBottomSheetItem(
                        id = i,
                        title = "Item $i",
                        group = group
                    )
                )
            }
        }

    private fun modalBottomSheetRadioButtonItems() =
        ModalBottomSheetFragment().apply {
            val groupId = 1000
            val group = ModalBottomSheetGroup(
                id = groupId,
                checkableBehavior = ModalBottomSheetGroup.CHECKABLE_BEHAVIOR_SINGLE,
                onItemCheckedChangeListener = onItemCheckedChangeListener
            )
            for (i in 1..10) {
                addItem(
                    ModalBottomSheetItem(
                        id = i,
                        title = "Item $i",
                        group = group
                    )
                )
            }
        }

    private fun modalBottomSheetDisabledEvenItems() =
        ModalBottomSheetFragment().apply {
            for (i in 1..10) {
                // Only disable on even items
                val disabled = i % 2 == 0
                addItem(
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
        ModalBottomSheetFragment().apply {
            for (i in 1..10) {
                // Only hide on odd items
                // This indicates that all even items are visible
                val visible = i % 2 == 0
                addItem(
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
        ModalBottomSheetFragment().apply {
            setItems {
                for (i in 1..10) {
                    item(title = "Item $i") {
                        id = i
                        onItemClickListener = this@DebugModalBottomSheetActivity.onItemClickListener
                    }
                }
                // plusAssign test
                this += item(title = "Item from plusAssign operator")
                this += {
                    title = "Item from plusAssign with DSL"
                }

                // Group
                group(id = 1) {
                    onItemCheckedChangeListener =
                        this@DebugModalBottomSheetActivity.onItemCheckedChangeListener
                    checkableBehavior = ModalBottomSheetGroup.CHECKABLE_BEHAVIOR_ALL
                }.apply {
                    items(buildList {
                        for (i in 11..15) {
                            this += item(title = "Item $i") {
                                id = i
                                onItemClickListener =
                                    this@DebugModalBottomSheetActivity.onItemClickListener
                            }
                        }
                    })
                }
                // Another group
                group(id = 2, {
                    id = 2
                    onItemCheckedChangeListener =
                        this@DebugModalBottomSheetActivity.onItemCheckedChangeListener
                    checkableBehavior = ModalBottomSheetGroup.CHECKABLE_BEHAVIOR_SINGLE
                }) {
                    items(buildList {
                        for (i in 21..25) {
                            this += item("Item $i") {
                                id = i
                                onItemClickListener =
                                    this@DebugModalBottomSheetActivity.onItemClickListener
                                setIcon(R.drawable.ic_info_outline_24dp)
                            }
                        }
                    })
                }
            }
        }

    private fun modalBottomSheetNoDraggable() = modalBottomSheet {
        hideDragHandle = true
        setItems {
            for (item in 1..5) {
                item("Item $item") {
                    id = item
                    onItemClickListener = this@DebugModalBottomSheetActivity.onItemClickListener
                }
            }
        }
    }
}
