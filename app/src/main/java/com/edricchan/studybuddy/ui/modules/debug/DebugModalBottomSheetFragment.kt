package com.edricchan.studybuddy.ui.modules.debug

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ListItemShapes
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.MimeTypeConstants
import com.edricchan.studybuddy.exts.android.showToast
import com.edricchan.studybuddy.exts.androidx.compose.plus
import com.edricchan.studybuddy.ui.common.fragment.ComposableFragment
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.dsl.showMultiSelectBottomSheet
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.dsl.showSingleSelectBottomSheet
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.ModalBottomSheetAdapter
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.interfaces.ModalBottomSheetItem
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.showModalBottomSheet
import kotlinx.coroutines.launch

// TODO: Move demo class to dedicated Gradle module
class DebugModalBottomSheetFragment : ComposableFragment() {

    @Immutable
    private data class DemoItem(
        val title: String,
        val onClick: () -> Unit
    )

    @Composable
    private fun rememberDemos(): List<DemoItem> = remember {
        listOf(
            DemoItem(
                "Modal bottom sheet with text items and no header",
                ::showModalBottomSheetWithTextNoHeader
            ),

            DemoItem(
                "Modal bottom sheet with text items and header",
                ::showModalBottomSheetWithTextAndHeader
            ),

            DemoItem(
                "Modal bottom sheet with icons and text items",
                ::showModalBottomSheetWithIconItems
            ),

            DemoItem(
                "Modal bottom sheet with 1000 text items",
                ::showModalBottomSheetWith1000Items
            ),

            DemoItem(
                "Modal bottom sheet with disabled even text items",
                ::showModalBottomSheetDisabledEvenItems
            ),

            DemoItem(
                "Modal bottom sheet with hidden odd text items",
                ::showModalBottomSheetHiddenOddItems
            ),

            DemoItem(
                "Modal bottom sheet with DSL syntax",
                ::showModalBottomSheetDSL
            ),

            DemoItem(
                "Modal bottom sheet without draggable handle",
                ::showModalBottomSheetNoDraggable
            ),

            // New select bottom sheet demos
            DemoItem("Select bottom sheet (multi)", ::showMultiSelect),

            DemoItem("Select bottom sheet (single)", ::showSingleSelect)
        )
    }

    private val snackbarHostState = SnackbarHostState()

    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    @Composable
    override fun Content(modifier: Modifier) {
        val demos = rememberDemos()

        Scaffold(
            modifier = modifier,
            contentWindowInsets = WindowInsets.navigationBars,
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier.consumeWindowInsets(innerPadding),
                contentPadding = innerPadding + PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)
            ) {
                itemsIndexed(demos) { i, item ->
                    DemoListItem(
                        shapes = ListItemDefaults.segmentedShapes(
                            index = i, count = demos.size
                        ),
                        iconRotationDeg = i * 15,
                        title = item.title,
                        onClick = item.onClick
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    @Composable
    private fun DemoListItem(
        modifier: Modifier = Modifier,
        shapes: ListItemShapes,
        iconRotationDeg: Int,
        title: String,
        onClick: () -> Unit
    ) {
        SegmentedListItem(
            modifier = modifier,
            onClick = onClick,
            leadingContent = {
                Surface(
                    shape = MaterialShapes.Cookie9Sided.toShape(startAngle = iconRotationDeg),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        modifier = Modifier.padding(4.dp),
                        imageVector = Icons.Outlined.PlayArrow, contentDescription = null
                    )
                }
            },
            content = {
                Text(text = title)
            },
            colors = ListItemDefaults.segmentedColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            shapes = shapes
        )
    }

    private fun showToast(item: ModalBottomSheetItem) {
        showToast("Item ${item.id} clicked!", Toast.LENGTH_SHORT)
    }

    private fun showSnackbar(text: String, duration: SnackbarDuration) {
        viewLifecycleOwner.lifecycleScope.launch {
            snackbarHostState.showSnackbar(
                message = text,
                duration = duration
            )
        }
    }

    private val onItemClickListener = ModalBottomSheetAdapter.OnItemClickListener { showToast(it) }
    private val onItemCheckedChangeListener =
        ModalBottomSheetAdapter.OnItemCheckedChangeListener { showToast(it) }

    private fun showModalBottomSheetWithTextNoHeader() {

        showModalBottomSheet {
            for (i in 1..10) {
                item(
                    ModalBottomSheetItem(
                        id = i, title = "Item $i",
                        onItemClickListener = onItemClickListener
                    )
                )
            }
        }
    }

    private fun showModalBottomSheetWithTextAndHeader() {
        showModalBottomSheet(
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
    }

    private fun showModalBottomSheetWith1000Items() {
        showModalBottomSheet {
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
    }

    private fun showModalBottomSheetWithIconItems() {
        showModalBottomSheet {
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
    }

    private fun showModalBottomSheetDisabledEvenItems() {
        showModalBottomSheet {
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
    }

    private fun showModalBottomSheetHiddenOddItems() {
        showModalBottomSheet {
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
    }

    private fun showModalBottomSheetDSL() {
        showModalBottomSheet {
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
    }

    private fun showModalBottomSheetNoDraggable() {
        showModalBottomSheet(
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

    private fun showMultiSelect() {
        showMultiSelectBottomSheet<Int>(
            headerTitle = "Select something",
            onCanceled = {
                showSnackbar(
                    "Multi-select bottom sheet was cancelled",
                    SnackbarDuration.Short
                )
            },
            onConfirm = { items ->
                showSnackbar(
                    "Items ${items.joinToString { it.id.toString() }} selected",
                    SnackbarDuration.Long
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

    private fun showSingleSelect() {
        showSingleSelectBottomSheet<Int>(
            headerTitle = "Select something",
            onCanceled = {
                showSnackbar(
                    "Single-select bottom sheet was cancelled",
                    SnackbarDuration.Short
                )
            },
            onConfirm = {
                showSnackbar(
                    "Item ${it.id} selected",
                    SnackbarDuration.Long
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
