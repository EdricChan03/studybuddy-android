package com.edricchan.studybuddy.constants

import android.net.Uri
import androidx.core.net.toUri
import com.edricchan.studybuddy.core.resources.metadata.StudyBuddyMetadata

object Constants {
    // Debug preferences
    const val debugUseTestingJsonUrl = "debug_updates_use_testing_json_url"
    const val debugSetCustomJsonUrl = "debug_updates_custom_json_url"

    // Actions
    const val actionNotificationsSettingsIntent =
        "com.edricchan.studybuddy.intent.ACTION_NOTIFICATIONS_SETTINGS_INTENT"
    const val actionNotificationsStartDownloadReceiver =
        "com.edricchan.studybuddy.receiver.ACTION_NOTIFICATIONS_START_DOWNLOAD_RECEIVER"
    const val actionNotificationsRetryCheckForUpdateReceiver =
        "com.edricchan.studybuddy.receiver.ACTION_NOTIFICATIONS_RETRY_CHECK_FOR_UPDATE_RECEIVER"

    // Notification IDs
    const val notificationCheckForUpdatesId = 0

    // TODO: Remove this constant
    const val notificationMediaId = 1

    // URIs
    const val urlHelpFeatured =
        "https://edricchan03.github.io/res/studybuddy/help-featured-articles.json"
    val uriHelpFeatured: Uri = urlHelpFeatured.toUri()
    const val urlAuthorWebsite = "https://edricchan03.github.io"
    val uriAuthorWebsite: Uri = urlAuthorWebsite.toUri()

    @Deprecated(
        "Use StudyBuddyMetadata.GitHubRepoUrl instead",
        ReplaceWith(
            "StudyBuddyMetadata.GitHubRepoUrl",
            "com.edricchan.studybuddy.core.resources.metadata.StudyBuddyMetadata"
        )
    )
    const val urlSrcCode = StudyBuddyMetadata.GitHubRepoUrl

    @Deprecated(
        "Use StudyBuddyMetadata.GitHubRepoUri instead",
        ReplaceWith(
            "StudyBuddyMetadata.GitHubRepoUri",
            "com.edricchan.studybuddy.core.resources.metadata.StudyBuddyMetadata"
        )
    )
    val uriSrcCode: Uri = StudyBuddyMetadata.GitHubRepoUri

    @Deprecated(
        "Use StudyBuddyMetadata.GitHubIssuesUrl instead",
        ReplaceWith(
            "StudyBuddyMetadata.GitHubIssuesUrl",
            "com.edricchan.studybuddy.core.resources.metadata.StudyBuddyMetadata"
        )
    )
    const val urlSrcBugTracker = StudyBuddyMetadata.GitHubIssuesUrl

    @Deprecated(
        "Use StudyBuddyMetadata.GitHubIssuesUri instead",
        ReplaceWith(
            "StudyBuddyMetadata.GitHubIssuesUri",
            "com.edricchan.studybuddy.core.resources.metadata.StudyBuddyMetadata"
        )
    )
    val uriSrcBugTracker: Uri = StudyBuddyMetadata.GitHubIssuesUri

    @Deprecated(
        "Use StudyBuddyMetadata.GitHubContributorsUrl instead",
        ReplaceWith(
            "StudyBuddyMetadata.GitHubContributorsUrl",
            "com.edricchan.studybuddy.core.resources.metadata.StudyBuddyMetadata"
        )
    )
    const val urlSrcContributors = StudyBuddyMetadata.GitHubContributorsUrl

    @Deprecated(
        "Use StudyBuddyMetadata.GitHubContributorsUri instead",
        ReplaceWith(
            "StudyBuddyMetadata.GitHubContributorsUri",
            "com.edricchan.studybuddy.core.resources.metadata.StudyBuddyMetadata"
        )
    )
    val uriSrcContributors: Uri = StudyBuddyMetadata.GitHubContributorsUri
    const val urlSendFeedback = "https://forms.gle/S6SY7aAgTyQUuKRw6"
    val uriSendFeedback: Uri = urlSendFeedback.toUri()
    const val urlSubmitTip = "https://goo.gl/forms/0agG0ObuQGPoZor92"
    val uriSubmitTip: Uri = urlSubmitTip.toUri()
}
