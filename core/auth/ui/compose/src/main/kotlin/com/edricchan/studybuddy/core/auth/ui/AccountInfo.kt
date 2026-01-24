package com.edricchan.studybuddy.core.auth.ui

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.core.auth.common.R
import com.edricchan.studybuddy.core.auth.model.User
import com.edricchan.studybuddy.core.auth.preview.UserPreviewParameterProvider
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
private fun AccountInfoMetadataTitleText(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.titleMedium
    )
}

@Composable
private fun AccountInfoMetadataSubtitleText(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.titleSmall
    )
}

@Composable
private fun AccountInfoMetadata(
    modifier: Modifier = Modifier,
    displayName: String?,
    email: String?
) = Column(modifier = modifier) {
    when {
        !email.isNullOrBlank() && !displayName.isNullOrBlank() -> {
            AccountInfoMetadataTitleText(
                text = displayName
            )
            AccountInfoMetadataSubtitleText(
                text = email
            )
        }

        // Show just the email address as title text if we don't have a display name present
        !email.isNullOrBlank() -> {
            AccountInfoMetadataTitleText(
                text = email
            )
        }

        // Show just the display name as title text if there's no email address
        !displayName.isNullOrBlank() -> {
            AccountInfoMetadataTitleText(
                text = displayName
            )
        }

        else -> {
            AccountInfoMetadataTitleText(
                text = stringResource(R.string.account_anon_display_name)
            )
        }
    }
}

@Composable
fun AccountInfoRow(
    modifier: Modifier = Modifier,
    displayName: String?,
    email: String?,
    photoUri: Uri?,
) = AccountInfoRow(
    modifier = modifier,
    profileImage = {
        ProfileImage(displayName = displayName, photoUri = photoUri)
    },
    metadata = {
        AccountInfoMetadata(
            displayName = displayName,
            email = email
        )
    }
)

@Composable
fun AccountInfoRow(
    modifier: Modifier = Modifier,
    user: User
) = AccountInfoRow(
    modifier = modifier,
    displayName = user.displayName,
    email = user.email,
    photoUri = user.photoUri
)

@Preview
@Composable
private fun AccountInfoRowPreview(@PreviewParameter(UserPreviewParameterProvider::class) user: User) {
    StudyBuddyTheme {
        AccountInfoRow(
            modifier = Modifier.padding(16.dp),
            user = user
        )
    }
}
