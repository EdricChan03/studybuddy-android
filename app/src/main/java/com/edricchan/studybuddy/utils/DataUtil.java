package com.edricchan.studybuddy.utils;

import android.net.Uri;

/**
 * Common properties to be used across the app
 */
public class DataUtil {
	// Preferences > General
	public static final String prefCategoryGeneral = "pref_category_general";
	public static final String prefAppTheme = "pref_app_theme";
	public static final String prefLanguage = "pref_language";
	/**
	 * @deprecated Use {@link DataUtil#prefLanguage} instead
	 */
	public static final String prefLocale = prefLanguage;
	// Preferences > Weekly summary
	public static final String prefCategoryWeeklySummary = "pref_category_weekly_summary";
	public static final String prefCategoryWeeklySummaryPrefs = "pref_category_weekly_summary_prefs";
	public static final String prefWeeklySummaryEnabled = "pref_weekly_summary_enabled";
	public static final String prefWeeklySummaryDay = "pref_weekly_summary_day";
	public static final String prefWeeklySummaryTime = "pref_weekly_summary_time";
	public static final String prefWeeklySummaryList = "pref_weekly_summary_list";

	// Actions
	public static final String actionNotificationsSettingsIntent = "com.edricchan.studybuddy.intent.ACTION_NOTIFICATIONS_SETTINGS_INTENT";
	public static final String actionNotificationsStartDownloadReceiver = "com.edricchan.studybuddy.receiver.ACTION_NOTIFICATIONS_START_DOWNLOAD_RECEIVER";
	public static final String actionNotificationsRetryCheckForUpdateReceiver = "com.edricchan.studybuddy.receiver.ACTION_NOTIFICATIONS_RETRY_CHECK_FOR_UPDATE_RECEIVER";

	// FCM-related strings
	public static final String fcmSettingsIcon = "settings";
	public static final String fcmNotificationIcon = "notification";
	public static final String fcmMarkAsDoneIcon = "mark_as_done";

	// Notification IDs
	public static final int notificationCheckForUpdatesId = 0;
	public static final int notificationMediaId = 1;

	// URIs
	public static final String urlSrcCode = "https://github.com/Chan4077/StudyBuddy";
	public static final Uri uriSrcCode = Uri.parse(urlSrcCode);
	public static final String urlWiki = "https://github.com/Chan4077/StudyBuddy/wiki";
	public static final Uri uriWiki = Uri.parse(urlWiki);
	public static final String urlSendFeedback = "https://goo.gl/forms/tz6cmNguIHuZMZIh2";
	public static final Uri uriSendFeedback = Uri.parse(urlSendFeedback);
	// Other
	public static final String defaultSharedPrefsFile = "Preference";
}
