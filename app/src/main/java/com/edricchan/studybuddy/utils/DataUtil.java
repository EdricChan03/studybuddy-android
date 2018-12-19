package com.edricchan.studybuddy.utils;

import android.net.Uri;

/**
 * Common properties to be used across the app
 */
public class DataUtil {
	// Preference headers
	public static final String prefHeaderGeneral = "pref_header_general";
	public static final String prefHeaderTodo = "pref_header_todo";
	public static final String prefHeaderSync = "pref_header_sync";
	public static final String prefHeaderDebug = "pref_header_debug";
	public static final String prefHeaderAbout = "pref_header_about";
	/**
	 * @deprecated Use {@link DataUtil#prefHeaderAbout} instead
	 */
	@Deprecated
	public static final String prefHeaderVersion = prefHeaderAbout;
	public static final String prefHeaderAccount = "pref_header_account";

	// Preferences
	public static final String prefShowHeaderSummary = "pref_show_header_summary";

	// Preferences > General
	public static final String prefCategoryGeneral = "pref_category_general";
	public static final String prefCategoryTheme = "pref_category_theme";
	public static final String prefDarkTheme = "pref_dark_theme";
	/**
	 * @deprecated Use {@link DataUtil#prefDarkTheme}
	 */
	@Deprecated
	public static final String prefAppTheme = prefDarkTheme;
	public static final String prefLanguage = "pref_language";
	public static final String prefUseCustomTabs = "pref_use_custom_tabs";
	public static final String prefEnableCrashlyticsUserTracking = "pref_enable_crashlytics_user_tracking";
	public static final String prefDayNightPermInfo = "pref_daynight_perm_info";
	public static final String prefDaynightGrantPerm = "pref_daynight_grant_perm";
	/**
	 * @deprecated Use {@link DataUtil#prefLanguage} instead
	 */
	@Deprecated
	public static final String prefLocale = prefLanguage;
	// Preferences > Weekly summary
	public static final String prefCategoryWeeklySummary = "pref_category_weekly_summary";
	public static final String prefCategoryWeeklySummaryPrefs = "pref_category_weekly_summary_prefs";
	public static final String prefWeeklySummaryEnabled = "pref_weekly_summary_enabled";
	public static final String prefWeeklySummaryDay = "pref_weekly_summary_day";
	public static final String prefWeeklySummaryTime = "pref_weekly_summary_time";
	public static final String prefWeeklySummaryList = "pref_weekly_summary_list";
	public static final String prefWeeklySummaryNotificationOpts = "pref_weekly_summary_notification_opts";
	// Preferences > About
	public static final String prefCategoryAbout = "pref_category_about";
	public static final String prefUpdates = "pref_updates";
	/**
	 * @deprecated Use {@link DataUtil#prefUpdates} instead
	 */
	@Deprecated
	public static final String prefUpdateActivity = prefUpdates;
	public static final String prefAppVersion = "pref_app_version";
	public static final String prefAppSrcCode = "pref_app_src_code";
	public static final String prefAppAuthor = "pref_app_author";
	public static final String prefAppInfo = "pref_app_info";

	// Debug preferences
	public static final String debugEnableVerboseLog = "debug_enable_verbose_log";
	public static final String debugDeviceInfo = "debug_device_info";
	public static final String debugAccountInfo = "debug_account_info";
	public static final String debugCrashApp = "debug_crash_app";
	public static final String debugSendNotification = "debug_send_notification";
	public static final String debugResetInstanceId = "debug_reset_instance_id";

	// Actions
	public static final String actionNotificationsSettingsIntent = "com.edricchan.studybuddy.intent.ACTION_NOTIFICATIONS_SETTINGS_INTENT";
	public static final String actionNotificationsStartDownloadReceiver = "com.edricchan.studybuddy.receiver.ACTION_NOTIFICATIONS_START_DOWNLOAD_RECEIVER";
	public static final String actionNotificationsRetryCheckForUpdateReceiver = "com.edricchan.studybuddy.receiver.ACTION_NOTIFICATIONS_RETRY_CHECK_FOR_UPDATE_RECEIVER";

	// FCM-related strings
	public static final String fcmSettingsIcon = "ic_settings_24dp";
	public static final String fcmNotificationIcon = "ic_notifications_24dp";
	public static final String fcmMarkAsDoneIcon = "ic_check_24dp";

	// Notification IDs
	public static final int notificationCheckForUpdatesId = 0;
	public static final int notificationMediaId = 1;

	// URIs
	public static final String urlHelpFeatured = "https://chan4077.github.io/res/studybuddy/help-featured-articles.json";
	public static final Uri uriHelpFeatured = Uri.parse(urlHelpFeatured);
	public static final String urlSrcCode = "https://github.com/Chan4077/StudyBuddy";
	public static final Uri uriSrcCode = Uri.parse(urlSrcCode);
	public static final String urlSendFeedback = "https://goo.gl/forms/tz6cmNguIHuZMZIh2";
	public static final Uri uriSendFeedback = Uri.parse(urlSendFeedback);
	public static final String urlSubmitTip = "https://goo.gl/forms/0agG0ObuQGPoZor92";
	public static final Uri uriSubmitTip = Uri.parse(urlSubmitTip);
	public static final String urlWiki = "https://github.com/Chan4077/StudyBuddy/wiki";
	public static final Uri uriWiki = Uri.parse(urlWiki);

	// Other
	public static final String defaultSharedPrefsFile = "Preference";
}
