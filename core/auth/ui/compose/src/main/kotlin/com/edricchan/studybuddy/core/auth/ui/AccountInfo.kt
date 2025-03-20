package com.edricchan.studybuddy.core.auth.ui

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.core.auth.common.R
import com.edricchan.studybuddy.core.auth.model.User
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme

@Composable
fun AccountInfoRow(
    modifier: Modifier = Modifier,
    profileImage: @Composable RowScope.() -> Unit,
    metadata: @Composable RowScope.() -> Unit
) = Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(16.dp)
) {
    profileImage()

    metadata()
}

@Composable
fun AccountInfoRow(
    modifier: Modifier = Modifier,
    displayName: String,
    email: String?,
    photoUri: Uri?,
) = AccountInfoRow(
    modifier = modifier,
    profileImage = {
        photoUri?.let {
            ProfileImage(displayName = displayName, photoUri = it)
        } ?: Icon(
            modifier = Modifier.fillMaxHeight(),
            imageVector = Icons.Outlined.AccountCircle,
            contentDescription = stringResource(R.string.account_profile_content_desc, displayName)
        )
    },
    metadata = {
        Column {
            Text(text = displayName, style = MaterialTheme.typography.titleMedium)
            email?.let { Text(text = it, style = MaterialTheme.typography.titleSmall) }
        }
    }
)

@Composable
fun AccountInfoRow(
    modifier: Modifier = Modifier,
    user: User
) = AccountInfoRow(
    modifier = modifier,
    displayName = user.displayName ?: stringResource(R.string.account_anon_display_name),
    email = user.email,
    photoUri = user.photoUri
)

@Preview(showBackground = true)
@Composable
private fun AccountInfoRowPreview() {
    StudyBuddyTheme {
        AccountInfoRow(
            modifier = Modifier.padding(16.dp),
            displayName = "John Appleseed",
            email = "example@example.com",
            photoUri = null
        )
    }
}
