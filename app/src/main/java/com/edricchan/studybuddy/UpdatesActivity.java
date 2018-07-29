package com.edricchan.studybuddy;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.edricchan.studybuddy.utils.CustomAlertDialogBuilder;
import com.github.javiersantos.appupdater.AppUpdaterUtils;
import com.github.javiersantos.appupdater.enums.AppUpdaterError;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.github.javiersantos.appupdater.objects.Update;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.Objects;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

public class UpdatesActivity extends AppCompatActivity {

	boolean isChecking = false;
	MaterialButton checkForUpdatesBtn;
	SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_updates);
		checkForUpdatesBtn = findViewById(R.id.empty_view_cta);
		checkForUpdatesBtn.setOnClickListener(click -> checkForUpdates());
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_updates, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_check_for_updates:
				Log.d(SharedHelper.getTag(getClass()), "Check for updates clicked!");
				Snackbar.make(findViewById(R.id.updatesView), R.string.update_snackbar_checking, Snackbar.LENGTH_SHORT)
						.show();
				checkForUpdates();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (isChecking) {
			menu.findItem(R.id.action_check_for_updates)
					.setEnabled(false);
		} else {
			menu.findItem(R.id.action_check_for_updates)
					.setEnabled(true);
		}
		return true;
	}

	private void downloadUpdate(String downloadUrl, String version) {
		downloadUpdate(downloadUrl, version, false, false);
	}

	private void downloadUpdate(String downloadUrl, String version, boolean mobileDataSkip, boolean downloadAgain) {
		String fileName = "com.edricchan.studybuddy-" + version + ".apk";
		if (downloadAgain || !new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + fileName).exists()) {
			if (mobileDataSkip || (SharedHelper.isCellularNetworkAvailable(UpdatesActivity.this) && preferences.getBoolean("updates_enable_cellular", false))) {
				AlertDialog.Builder builder = new AlertDialog.Builder(UpdatesActivity.this);
				builder.setTitle(R.string.update_activity_cannot_download_cellular_dialog_title);
				builder.setMessage(R.string.update_activity_cannot_download_cellular_dialog_msg);
				builder.setNegativeButton(R.string.dialog_action_cancel, (DialogInterface dialogInterface, int i) -> dialogInterface.dismiss());
				builder.setPositiveButton(R.string.update_activity_cannot_download_cellular_dialog_positive_btn, (dialogInterface, i) -> {
					dialogInterface.dismiss();
					downloadUpdate(downloadUrl, version, true, false);
				});
				builder.show();
			} else {
				DownloadManager.Request request1 = new DownloadManager.Request(Uri.parse(downloadUrl));

				request1.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
				request1.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
				request1.setMimeType("application/vnd.android.package-archive");
				DownloadManager manager1 = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
				Objects.requireNonNull(manager1).enqueue(request1);
				if (DownloadManager.STATUS_SUCCESSFUL == 8) {
					Toast.makeText(getApplicationContext(), "Download complete!", Toast.LENGTH_SHORT).show();
					installUpdate(fileName);
				}
			}
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(UpdatesActivity.this);
			builder.setTitle(R.string.update_activity_update_already_downloaded_dialog_title);
			builder.setMessage(R.string.update_activity_update_already_downloaded_dialog_msg);
			builder.setPositiveButton(R.string.update_activity_update_already_downloaded_dialog_positive_btn, (DialogInterface dialogInterface, int i) -> {
				installUpdate(fileName);
				dialogInterface.dismiss();
			});
			builder.setNegativeButton(R.string.update_activity_update_already_downloaded_dialog_negative_btn, (DialogInterface dialogInterface, int i) -> {
				if (new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + fileName).delete()) {
					Toast.makeText(UpdatesActivity.this, R.string.update_activity_delete_update_success_toast, Toast.LENGTH_LONG).show();
					downloadUpdate(downloadUrl, version, false, true);
				} else {
					Toast.makeText(UpdatesActivity.this, R.string.update_activity_cannot_delete_update_toast, Toast.LENGTH_LONG).show();
				}
			});
			builder.show();
		}
	}

	private void installUpdate(String fileName) {
		// See https://android-developers.googleblog.com/2017/08/making-it-safer-to-get-apps-on-android-o.html
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

			if (UpdatesActivity.this.getPackageManager().canRequestPackageInstalls()) {
				Intent installIntent = new Intent(Intent.ACTION_VIEW);
				installIntent.setDataAndType(FileProvider.getUriForFile(getApplicationContext(), "com.edricchan.studybuddy.provider", new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + fileName)), "application/vnd.android.package-archive");
				installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// Allow reading the file
				installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
				startActivity(installIntent);
			} else {
				CustomAlertDialogBuilder builder = new CustomAlertDialogBuilder(UpdatesActivity.this);
				builder.setMessage(R.string.update_activity_enable_unknown_sources_dialog_msg);
				builder.setNegativeButton(R.string.dialog_action_cancel, (DialogInterface dialogInterface, int i) -> dialogInterface.dismiss());
				builder.setNeutralButton(R.string.dialog_action_retry, (DialogInterface dialogInterface, int i) -> {
					dialogInterface.dismiss();
					installUpdate(fileName);
				});
				builder.setPositiveButton(R.string.update_activity_enable_unknown_sources_dialog_positive_btn, (DialogInterface dialogInterface, int i) -> {
					Intent allowUnknownAppsIntent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
					allowUnknownAppsIntent.setData(Uri.parse("package:com.edricchan.studybuddy"));
					startActivity(allowUnknownAppsIntent);
				});
				builder.show();
			}
		} else {
			Intent installIntent = new Intent(Intent.ACTION_VIEW);
			installIntent.setDataAndType(FileProvider.getUriForFile(getApplicationContext(), "com.edricchan.studybuddy.provider", new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + fileName)), "application/vnd.android.package-archive");
			installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// Allow reading the file
			installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			startActivity(installIntent);
		}
	}

	private void checkForUpdates() {
		isChecking = true;
		invalidateOptionsMenu();
		final AppUpdaterUtils appUpdaterUtils = new AppUpdaterUtils(this)
				.setUpdateFrom(UpdateFrom.JSON)
				.setUpdateJSON(getString(R.string.testing_changelog_url))
				.withListener(new AppUpdaterUtils.UpdateListener() {
					@Override
					public void onSuccess(final Update update, Boolean updateAvailable) {
						if (update.getLatestVersionCode() == BuildConfig.VERSION_CODE && !updateAvailable) {
							// User is running latest version
							Snackbar.make(findViewById(R.id.updatesView), R.string.update_snackbar_latest, Snackbar.LENGTH_SHORT)
									.show();
						} else {
							// New update
							AlertDialog.Builder builder = new AlertDialog.Builder(UpdatesActivity.this);
							builder.setTitle(String.format(getString(R.string.update_dialog_title_new), update.getLatestVersion()));
							builder.setIcon(R.drawable.ic_system_update_24dp);
							builder.setMessage("What's new:\n" + update.getReleaseNotes());
							builder.setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> {

							});
							builder.setPositiveButton(R.string.update_dialog_positive_btn_text, (dialogInterface, i) -> {
								if (SharedHelper.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, UpdatesActivity.this)) {
									downloadUpdate(update.getUrlToDownload().toString(), update.getLatestVersion());
								}
							});
							builder.setOnDismissListener(dialogInterface -> {
								isChecking = false;
								invalidateOptionsMenu();
							});
							builder.setOnCancelListener(dialogInterface -> {
								isChecking = false;
								invalidateOptionsMenu();
							});
							builder.create().show();
						}
					}

					@Override
					public void onFailed(AppUpdaterError appUpdaterError) {
						isChecking = false;
						invalidateOptionsMenu();
						switch (appUpdaterError) {
							case NETWORK_NOT_AVAILABLE:
								Snackbar.make(findViewById(R.id.updatesView), R.string.update_snackbar_error_no_internet, Snackbar.LENGTH_LONG)
										.setAction(R.string.dialog_action_retry, view -> checkForUpdates())
										.show();
								break;
							case JSON_ERROR:
								Snackbar.make(findViewById(R.id.updatesView), R.string.update_snackbar_error_malformed, Snackbar.LENGTH_LONG)
										.setAction(R.string.dialog_action_retry, view -> checkForUpdates())
										.show();
								break;
							default:
								Snackbar.make(findViewById(R.id.updatesView), R.string.update_snackbar_error, Snackbar.LENGTH_LONG)
										.setAction(R.string.dialog_action_retry, view -> checkForUpdates())
										.show();
								break;
						}
					}
				});
		appUpdaterUtils.start();
	}
}
