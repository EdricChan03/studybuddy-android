package com.edricchan.studybuddy.features.settings.main.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.core.auth.LocalCurrentUser
import com.edricchan.studybuddy.core.auth.model.User
import com.edricchan.studybuddy.core.auth.ui.AccountInfoRow
import com.edricchan.studybuddy.core.resources.icons.AppIcons
import com.edricchan.studybuddy.core.resources.icons.outlined.Info
import com.edricchan.studybuddy.features.settings.R
import com.edricchan.studybuddy.features.settings.navigation.SettingsCategory
import com.edricchan.studybuddy.features.settings.navigation.SettingsListItem
import com.edricchan.studybuddy.ui.theming.compose.theme.preview.StudyBuddyThemeWrapperProvider

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingsListScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    currentUser: User? = LocalCurrentUser.current,
    selectedItem: SettingsCategory?,
    onNavigateToDetail: (SettingsCategory) -> Unit,
    onNavigateToAccount: () -> Unit,
    onNavigateToAbout: () -> Unit
) {
    // Count is added by one to account for the "About" item
    val entryCount = remember { SettingsCategory.entries.size + 1 }

    LazyColumn(
        modifier = modifier.selectableGroup(),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)
    ) {
        currentUser?.let {
            item {
                AccountListItem(
                    onNavigateToAccount = onNavigateToAccount,
                    user = it
                )
            }
        }

        itemsIndexed(SettingsCategory.entries) { i, item ->
            item.SettingsListItem(
                onClick = { onNavigateToDetail(item) },
                selected = selectedItem == item,
                shapes = ListItemDefaults.segmentedShapes(
                    index = i,
                    count = entryCount
                )
            )
        }

        // About item
        item {
            SegmentedListItem(
                onClick = onNavigateToAbout,
                shapes = ListItemDefaults.segmentedShapes(
                    // Last index of the entries is added by one to account for the "About" item
                    index = SettingsCategory.entries.lastIndex + 1,
                    count = entryCount
                ),
                content = { Text(text = stringResource(R.string.pref_header_about_title)) },
                leadingContent = { Icon(AppIcons.Outlined.Info, contentDescription = null) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun AccountListItem(
    modifier: Modifier = Modifier,
    user: User,
    onNavigateToAccount: () -> Unit,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp - ListItemDefaults.SegmentedGap),
        onClick = onNavigateToAccount,
        shape = CardDefaults.outlinedShape,
        border = CardDefaults.outlinedCardBorder()
    ) {
        AccountInfoRow(modifier = Modifier.padding(16.dp), user = user)
    }
}

@Preview
@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
@Composable
private fun SettingsListScreenPreview() {
    SettingsListScreen(
        selectedItem = SettingsCategory.General,
        onNavigateToDetail = {},
        onNavigateToAccount = {},
        onNavigateToAbout = {}
    )
}
