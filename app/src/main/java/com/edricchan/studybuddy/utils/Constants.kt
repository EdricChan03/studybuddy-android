package com.edricchan.studybuddy.utils

import android.net.Uri

object Constants {
	// Preference headers
	const val prefHeaderGeneral = "pref_header_general"
	const val prefHeaderTodo = "pref_header_todo"
	const val prefHeaderSync = "pref_header_sync"
	const val prefHeaderDebug = "pref_header_debug"
	const val prefHeaderAbout = "pref_header_about"
	@Deprecated(
			"Use Constants.prefHeaderAbout",
			ReplaceWith(
					"Constants.prefHeaderAbout",
					"com.edricchan.studybuddy.utils.Constants"
			)
	)
	const val prefHeaderVersion = prefHeaderAbout
	const val prefHeaderAccount = "pref_header_account"

	// Preferences
	const val prefShowHeaderSummary = "pref_show_header_summary"

	// Preferences > General
	const val prefCategoryGeneral = "pref_category_general"
	const val prefCategoryTheme = "pref_category_theme"
	const val prefDarkTheme = "pref_dark_theme"
	const val prefDarkThemeAuto = "automatic"
	const val prefDarkThemeAutoTime = "automatic_time"
	const val prefDarkThemeAutoBatterySaver = "automatic_battery_saver"
	const val prefDarkThemeAlways = "always"
	const val prefDarkThemeFollowSystem = "follow_system"
	const val prefDarkThemeNever = "never"

	@Deprecated(
			"Use Constants.prefDarkTheme",
			ReplaceWith(
					"Constants.prefAppTheme",
					"com.edricchan.studybuddy.utils.Constants"
			)
	)
	const val prefAppTheme = prefDarkTheme
	const val prefLanguage = "pref_language"
	const val prefUseCustomTabs = "pref_use_custom_tabs"
	const val prefEnableCrashlyticsUserTracking = "pref_enable_crashlytics_user_tracking"
	const val prefDayNightPermInfo = "pref_daynight_perm_info"
	const val prefDaynightGrantPerm = "pref_daynight_grant_perm"

	@Deprecated(
			"Use Constants.prefLanguage",
			ReplaceWith(
					"Constants.prefLanguage",
					"com.edricchan.studybuddy.utils.Constants"
			)
	)
	const val prefLocale = prefLanguage
	// Preferences > Weekly summary
	const val prefCategoryWeeklySummary = "pref_category_weekly_summary"
	const val prefCategoryWeeklySummaryPrefs = "pref_category_weekly_summary_prefs"
	const val prefWeeklySummaryEnabled = "pref_weekly_summary_enabled"
	const val prefWeeklySummaryDay = "pref_weekly_summary_day"
	const val prefWeeklySummaryTime = "pref_weekly_summary_time"
	const val prefWeeklySummaryList = "pref_weekly_summary_list"
	const val prefWeeklySummaryNotificationOpts = "pref_weekly_summary_notification_opts"
	// Preferences > About
	const val prefCategoryAbout = "pref_category_about"
	const val prefUpdates = "pref_updates"
	const val prefAboutAppAuthor = "pref_about_app_author"
	const val prefAboutAppInfo = "pref_about_app_info"
	const val prefAboutAppVersion = "pref_about_app_version"
	const val prefAboutLicenses = "pref_about_licenses"
	const val prefAboutSourceCode = "pref_about_source_code"

	@Deprecated(
			"Use Constants.prefUpdates",
			ReplaceWith(
					"Constants.prefUpdates",
					"com.edricchan.studybuddy.utils.Constants"
			)
	)
	const val prefUpdateActivity = prefUpdates
	@Deprecated(
			"Use Constants.prefAboutAppVersion",
			ReplaceWith(
					"Constants.prefAboutAppVersion",
					"com.edricchan.studybuddy.utils.Constants"
			)
	)
	const val prefAppVersion = prefAboutAppVersion

	@Deprecated(
			"Use Constants.prefAboutSourceCode",
			ReplaceWith(
					"Constants.prefAboutSourceCode",
					"com.edricchan.studybuddy.utils.Constants"
			)
	)
	const val prefAppSrcCode = prefAboutSourceCode
	@Deprecated(
			"Use Constants.prefAboutAppAuthor",
			ReplaceWith(
					"Constants.prefAboutAppAuthor",
					"com.edricchan.studybuddy.utils.Constants"
			)
	)
	const val prefAppAuthor = prefAboutAppAuthor
	@Deprecated(
			"Use Constants.prefAboutAppInfo",
			ReplaceWith(
					"Constants.prefAboutAppInfo",
					"com.edricchan.studybuddy.utils.Constants"
			)
	)
	const val prefAppInfo = prefAboutAppInfo

	// Debug preferences
	const val debugEnableVerboseLog = "debug_enable_verbose_log"
	const val debugDeviceInfo = "debug_device_info"
	const val debugAccountInfo = "debug_account_info"
	const val debugCrashApp = "debug_crash_app"
	const val debugSendNotification = "debug_send_notification"
	const val debugResetInstanceId = "debug_reset_instance_id"
	const val debugUseTestingJsonUrl = "debug_updates_use_testing_json_url"
	const val debugSetCustomJsonUrl = "debug_updates_custom_json_url"
	const val debugUpdatesUpdateMetadata = "debug_updates_update_metadata"
	const val debugUpdatesLastCheckedForUpdatesDate = "debug_updates_last_checked_for_updates_date"
	const val debugUpdatesClearLastCheckedForUpdatesDate = "debug_updates_clear_last_checked_for_updates_date"
	const val debugUpdatesLastUpdatedDate = "debug_updates_last_updated_date"
	const val debugUpdatesClearLastUpdatedDate = "debug_updates_clear_last_updated_date"

	// Actions
	const val actionNotificationsSettingsIntent = "com.edricchan.studybuddy.intent.ACTION_NOTIFICATIONS_SETTINGS_INTENT"
	const val actionNotificationsStartDownloadReceiver = "com.edricchan.studybuddy.receiver.ACTION_NOTIFICATIONS_START_DOWNLOAD_RECEIVER"
	const val actionNotificationsRetryCheckForUpdateReceiver = "com.edricchan.studybuddy.receiver.ACTION_NOTIFICATIONS_RETRY_CHECK_FOR_UPDATE_RECEIVER"

	// FCM-related strings
	const val fcmSettingsIcon = "ic_settings_24dp"
	const val fcmNotificationIcon = "ic_notifications_24dp"
	const val fcmMarkAsDoneIcon = "ic_check_24dp"

	// Notification IDs
	const val notificationCheckForUpdatesId = 0
	const val notificationMediaId = 1

	// URIs
	const val urlHelpFeatured = "https://edricchan03.github.io/res/studybuddy/help-featured-articles.json"
	val uriHelpFeatured: Uri = Uri.parse(urlHelpFeatured)
	const val urlAuthorWebsite = "https://edricchan03.github.io"
	val uriAuthorWebsite: Uri = Uri.parse(urlAuthorWebsite)
	const val urlSrcCode = "https://github.com/EdricChan03/StudyBuddy"
	val uriSrcCode: Uri = Uri.parse(urlSrcCode)
	const val urlSendFeedback = "https://goo.gl/forms/tz6cmNguIHuZMZIh2"
	val uriSendFeedback: Uri = Uri.parse(urlSendFeedback)
	const val urlSubmitTip = "https://goo.gl/forms/0agG0ObuQGPoZor92"
	val uriSubmitTip: Uri = Uri.parse(urlSubmitTip)
	const val urlWiki = "https://github.com/EdricChan03/StudyBuddy/wiki"
	val uriWiki: Uri = Uri.parse(urlWiki)
	// Other
	/**
	 * The default file name to be used for SharedPreferences files
	 */
	const val defaultSharedPrefsFile = "Preference"
	/**
	 * The authority to be used for [androidx.core.content.FileProvider.getUriForFile]
	 */
	const val fileProviderAuthority = "com.edricchan.studybuddy.provider"
}
