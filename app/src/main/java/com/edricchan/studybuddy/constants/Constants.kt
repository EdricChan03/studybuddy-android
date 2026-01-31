package com.edricchan.studybuddy.constants

import android.net.Uri
import androidx.core.net.toUri

object Constants {
    // Preferences > Updates
    const val prefUpdatesFrequency = "pref_updates_frequency"
    const val prefUpdatesDownloadOverMetered = "pref_updates_download_over_metered"
    const val prefUpdatesDownloadOnlyWhenCharging = "pref_updates_download_only_when_charging"

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
    const val urlSrcCode = "https://github.com/EdricChan03/StudyBuddy-android"
    val uriSrcCode: Uri = urlSrcCode.toUri()
    const val urlSrcBugTracker = "$urlSrcCode/issues"
    val uriSrcBugTracker: Uri = urlSrcBugTracker.toUri()
    const val urlSrcContributors = "$urlSrcCode/graph/contributors"
    val uriSrcContributors: Uri = urlSrcContributors.toUri()
    const val urlSendFeedback = "https://forms.gle/S6SY7aAgTyQUuKRw6"
    val uriSendFeedback: Uri = urlSendFeedback.toUri()
    const val urlSubmitTip = "https://goo.gl/forms/0agG0ObuQGPoZor92"
    val uriSubmitTip: Uri = urlSubmitTip.toUri()
    const val urlWiki = "https://github.com/EdricChan03/StudyBuddy-android/wiki"
    val uriWiki: Uri = urlWiki.toUri()
}
