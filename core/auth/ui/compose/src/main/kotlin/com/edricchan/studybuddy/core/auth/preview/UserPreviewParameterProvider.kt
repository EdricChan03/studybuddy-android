package com.edricchan.studybuddy.core.auth.preview

import android.annotation.SuppressLint
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.edricchan.studybuddy.core.auth.model.User
import com.edricchan.studybuddy.core.auth.model.testing.UserFakes

@SuppressLint("VisibleForTests")
private val fakes = listOf(
    UserFakes.WithNoData,
    UserFakes.OnlyEmail,
    UserFakes.OnlyDisplayName,
    UserFakes.DisplayNameAndEmail
)

/**
 * [androidx.compose.ui.tooling.preview.PreviewParameterProvider] supplying fake [User]
 * data.
 */
class UserPreviewParameterProvider : CollectionPreviewParameterProvider<User>(
    fakes
) {
    override fun getDisplayName(index: Int): String = fakes[index].toString()
}
