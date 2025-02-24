package com.edricchan.studybuddy.constants

import android.net.Uri
import androidx.core.net.toUri
import com.edricchan.studybuddy.BuildConfig

object Constants {
    // Preference headers
    const val prefHeaderGeneral = "pref_header_general"
    const val prefHeaderTodo = "pref_header_todo"
    const val prefHeaderDebug = "pref_header_debug"
    const val prefHeaderUpdates = "pref_header_updates"
    const val prefHeaderAbout = "pref_header_about"
    const val prefHeaderAccount = "pref_header_account"

    // Preferences
    const val prefShowHeaderSummary = "pref_show_header_summary"

    // Preferences > General
    @Deprecated(
        "Use the top-level keyPrefEnableUserTracking property",
        ReplaceWith(
            "keyPrefEnableUserTracking",
            "com.edricchan.studybuddy.core.settings.tracking.keyPrefEnableUserTracking"
        )
    )
    const val prefEnableCrashlyticsUserTracking = "pref_enable_crashlytics_user_tracking"

    // Prefrences > Updates
    const val prefUpdates = "pref_updates"
    const val prefUpdatesCategoryOptions = "pref_updates_options_category"
    const val prefUpdatesFrequency = "pref_updates_frequency"
    const val prefUpdatesDownloadOverMetered = "pref_updates_download_over_metered"
    const val prefUpdatesDownloadOnlyWhenCharging = "pref_updates_download_only_when_charging"

    // Preferences > About
    const val prefCategoryAbout = "pref_category_about"
    const val prefAboutAppAuthor = "pref_about_app_author"
    const val prefAboutAppInfo = "pref_about_app_info"
    const val prefAboutAppVersion = "pref_about_app_version"
    const val prefAboutAppVersionCode = "pref_about_app_version_code"
    const val prefAboutAppBuildVariant = "pref_about_app_build_variant"
    const val prefAboutLicenses = "pref_about_licenses"
    const val prefAboutSourceCode = "pref_about_source_code"

    // Debug preferences
    const val debugDevModeEnabled = "debug_dev_mode_enabled"
    const val debugFeatureFlags = "debug_feature_flags"
    const val debugDeviceInfo = "debug_device_info"
    const val debugAccountInfo = "debug_account_info"
    const val debugCrashApp = "debug_crash_app"
    const val debugSendNotification = "debug_send_notification"
    const val debugResetInstanceId = "debug_reset_instance_id"
    const val debugUseTestingJsonUrl = "debug_updates_use_testing_json_url"
    const val debugSetCustomJsonUrl = "debug_updates_custom_json_url"
    const val debugUpdatesUpdateMetadata = "debug_updates_update_metadata"
    const val debugUpdatesStartWorker = "debug_updates_start_worker"
    const val debugUpdatesLastCheckedForUpdatesDate = "debug_updates_last_checked_for_updates_date"
    const val debugUpdatesClearLastCheckedForUpdatesDate =
        "debug_updates_clear_last_checked_for_updates_date"
    const val debugUpdatesLastUpdatedDate = "debug_updates_last_updated_date"
    const val debugUpdatesClearLastUpdatedDate = "debug_updates_clear_last_updated_date"
    const val debugOtherClearAppSettings = "debug_other_clear_app_settings"
    const val debugOtherModalBottomSheetTesting = "debug_other_modal_bottom_sheet_testing"

    // Feature flags
    const val featureFlagsReset = "feature_flags_reset"
    const val featureFlagsCalendarEnabled = "feature_flags_calendar_enabled"
    const val featureFlagsInterfaceV2Enabled = "feature_flags_interface_v2_enabled"
    const val featureFlagsAboutAppV2Enabled = "feature_flags_about_app_v2_enabled"

    // Actions
    const val actionNotificationsSettingsIntent =
        "com.edricchan.studybuddy.intent.ACTION_NOTIFICATIONS_SETTINGS_INTENT"
    const val actionNotificationsStartDownloadReceiver =
        "com.edricchan.studybuddy.receiver.ACTION_NOTIFICATIONS_START_DOWNLOAD_RECEIVER"
    const val actionNotificationsRetryCheckForUpdateReceiver =
        "com.edricchan.studybuddy.receiver.ACTION_NOTIFICATIONS_RETRY_CHECK_FOR_UPDATE_RECEIVER"

    // FCM-related strings
    const val fcmSettingsIcon = "ic_settings_24dp"
    const val fcmNotificationIcon = "ic_notifications_24dp"
    const val fcmMarkAsDoneIcon = "ic_check_24dp"

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
    const val urlSendFeedback = "https://forms.gle/S6SY7aAgTyQUuKRw6"
    val uriSendFeedback: Uri = urlSendFeedback.toUri()
    const val urlSubmitTip = "https://goo.gl/forms/0agG0ObuQGPoZor92"
    val uriSubmitTip: Uri = urlSubmitTip.toUri()
    const val urlWiki = "https://github.com/EdricChan03/StudyBuddy-android/wiki"
    val uriWiki: Uri = urlWiki.toUri()

    // Other
    /**
     * The default file name to be used for SharedPreferences files
     */
    const val defaultSharedPrefsFile = "Preference"

    /**
     * The authority to be used for [androidx.core.content.FileProvider.getUriForFile]
     */
    const val fileProviderAuthority = "${BuildConfig.APPLICATION_ID}.provider"
}
