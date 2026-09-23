package com.edricchan.studybuddy.ui.modules.main.nav

import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.core.auth.ui.AccountInfoRow
import com.edricchan.studybuddy.core.compat.navigation.CompatDestination
import com.edricchan.studybuddy.core.resources.icons.AppIcons
import com.edricchan.studybuddy.core.resources.icons.outlined.Event
import com.edricchan.studybuddy.core.resources.icons.outlined.Task
import com.edricchan.studybuddy.features.tasks.navigation.TaskDestination

@Composable
fun NavModalBottomSheetHeader(
    modifier: Modifier = Modifier,
    photoUrl: Uri?,
    displayName: String?,
    email: String?
) = AccountInfoRow(
    modifier = modifier.padding(16.dp),
    displayName = displayName,
    photoUri = photoUrl,
    email = email
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavModalBottomSheetContent(
    modifier: Modifier = Modifier,
    headerModifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
    selectedItem: NavItem?,
    onItemClick: (NavItem) -> Unit,
    photoUrl: Uri?,
    displayName: String?,
    email: String?
) = Surface(
    color = DrawerDefaults.modalContainerColor
) {
    LazyColumn(
        modifier = modifier.selectableGroup(),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        stickyHeader {
            Column {
                BottomSheetDefaults.DragHandle(modifier = Modifier.align(Alignment.CenterHorizontally))

                NavModalBottomSheetHeader(
                    modifier = headerModifier.fillMaxWidth(),
                    photoUrl = photoUrl,
                    displayName = displayName,
                    email = email
                )
            }
        }
        item {
            HorizontalDivider()
        }
        items(NavItem.entries) {
            MainNavItem(
                item = it,
                selected = it == selectedItem,
                onClick = {
                    onItemClick(it)
                }
            )
        }
    }
}

@Composable
private fun MainNavItem(
    modifier: Modifier = Modifier,
    item: NavItem,
    selected: Boolean,
    onClick: () -> Unit
) = NavigationDrawerItem(
    modifier = modifier,
    selected = selected,
    onClick = onClick,
    label = {
        Text(text = stringResource(item.titleRes))
    },
    icon = {
        Icon(item.icon, contentDescription = null)
    }
)

enum class NavItem {
    Tasks,
    Calendar
}

@get:StringRes
val NavItem.titleRes: Int
    get() = when (this) {
        NavItem.Tasks -> R.string.bottom_nav_todos
        NavItem.Calendar -> R.string.bottom_nav_calendar
    }

val NavItem.icon: ImageVector
    get() = when (this) {
        NavItem.Tasks -> AppIcons.Outlined.Task
        NavItem.Calendar -> AppIcons.Outlined.Event
    }

fun NavController.currentNavItem(): NavItem? {
    val dest = currentDestination ?: return null

    return when {
        dest.hasRoute<CompatDestination.Calendar>() -> NavItem.Calendar
        dest.hasRoute<TaskDestination.TaskGraphRoot>() || dest.hierarchy.any { it.hasRoute<TaskDestination.TaskGraphRoot>() } -> NavItem.Tasks
        else -> null
    }
}
