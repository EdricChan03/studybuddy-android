package com.edricchan.studybuddy.core.auth.model.testing

import android.net.Uri
import androidx.annotation.VisibleForTesting
import com.edricchan.studybuddy.core.auth.model.User
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@VisibleForTesting
object UserFakes {
    @OptIn(ExperimentalUuidApi::class)
    private fun generateId() = Uuid.random().toString()

    private fun createUser(
        displayName: String? = null,
        email: String? = null,
        photoUri: Uri? = null
    ): User = User(
        id = generateId(),
        displayName = displayName,
        email = email,
        photoUri = photoUri
    )

    const val EmailText = "johnappleseed@example.com"
    const val DisplayNameText = "John Appleseed"

    /** Test [User] class with no data. */
    @VisibleForTesting
    val WithNoData: User = createUser()

    /** Test [User] class with only an email address present. */
    @VisibleForTesting
    val OnlyEmail: User = createUser(
        email = EmailText
    )

    /** Test [User] class with only a display name present. */
    @VisibleForTesting
    val OnlyDisplayName: User = createUser(
        displayName = DisplayNameText
    )

    /** Test [User] class with a display name and email address present. */
    @VisibleForTesting
    val DisplayNameAndEmail: User = createUser(
        displayName = DisplayNameText,
        email = EmailText
    )
}
