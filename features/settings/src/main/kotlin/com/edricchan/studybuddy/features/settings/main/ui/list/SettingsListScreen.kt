package com.edricchan.studybuddy.features.settings.main.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.core.auth.LocalCurrentUser
import com.edricchan.studybuddy.core.auth.model.User
import com.edricchan.studybuddy.core.auth.ui.AccountInfoRow
import com.edricchan.studybuddy.features.settings.R
import com.edricchan.studybuddy.features.settings.navigation.SettingsCategory
import com.edricchan.studybuddy.features.settings.navigation.SettingsListItem
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme

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
    LazyColumn(
        modifier = modifier.selectableGroup(),
        contentPadding = contentPadding
    ) {
        currentUser?.let {
            item {
                AccountListItem(
                    onNavigateToAccount = onNavigateToAccount,
                    user = it
                )
            }
        }

        items(SettingsCategory.entries) {
            it.SettingsListItem(
                onClick = { onNavigateToDetail(it) },
                selected = selectedItem == it
            )
        }

        // About item
        item {
            ListItem(
                modifier = Modifier
                    .clip(CardDefaults.outlinedShape)
                    .clickable(onClick = onNavigateToAbout),
                headlineContent = { Text(text = stringResource(R.string.pref_header_about_title)) },
                leadingContent = { Icon(Icons.Outlined.Info, contentDescription = null) }
            )
        }
    }
}

@Composable
private fun AccountListItem(
    modifier: Modifier = Modifier,
    user: User,
    onNavigateToAccount: () -> Unit,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        onClick = onNavigateToAccount,
        shape = CardDefaults.outlinedShape,
        border = CardDefaults.outlinedCardBorder()
    ) {
        AccountInfoRow(modifier = Modifier.padding(16.dp), user = user)
    }
}

@Preview
@Composable
private fun SettingsListScreenPreview() {
    StudyBuddyTheme {
        SettingsListScreen(
            selectedItem = SettingsCategory.General,
            onNavigateToDetail = {},
            onNavigateToAccount = {},
            onNavigateToAbout = {}
        )
    }
}
