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
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.edricchan.studybuddy.utils.SharedUtils;
import com.github.javiersantos.appupdater.AppUpdaterUtils;
import com.github.javiersantos.appupdater.enums.AppUpdaterError;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.github.javiersantos.appupdater.objects.Update;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.Objects;

public class UpdatesActivity extends AppCompatActivity {

	boolean isChecking = false;
	Update appUpdate;
	Button checkForUpdatesBtn;
	RelativeLayout emptyStateLayout;
	SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_updates);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		checkForUpdatesBtn = findViewById(R.id.empty_state_view_cta);
		checkForUpdatesBtn.setOnClickListener(click -> checkForUpdates());
		emptyStateLayout = findViewById(R.id.updates_empty_state_view);
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
			case android.R.id.home:
				onBackPressed();
				return true;
			case R.id.action_check_for_updates:
				Log.d(SharedUtils.Companion.getTag(getClass()), "Check for updates clicked!");
				Snackbar.make(findViewById(R.id.updatesView), R.string.update_snackbar_checking, Snackbar.LENGTH_SHORT)
						.show();
				checkForUpdates();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
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

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == 0) {
			showUpdateDialog();
		}
	}

	private void downloadUpdate(String downloadUrl, String version) {
		downloadUpdate(downloadUrl, version, false, false);
	}

	private void downloadUpdate(String downloadUrl, String version, boolean mobileDataSkip, boolean downloadAgain) {
		String fileName = "com.edricchan.studybuddy-" + version + ".apk";
		if (downloadAgain || !new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + fileName).exists()) {
			if (mobileDataSkip || SharedUtils.Companion.isCellularNetworkAvailable(this)) {
				MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
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

				request1.setAllowedOverRoaming(false);
				request1.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
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
			MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
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
				MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
				builder.setMessage(R.string.update_activity_enable_unknown_sources_dialog_msg)
						.setNegativeButton(R.string.dialog_action_cancel, (dialog, which) -> dialog.dismiss())
						.setNeutralButton(R.string.dialog_action_retry, (dialog, which) -> {
							dialog.dismiss();
							installUpdate(fileName);
						})
						.setPositiveButton(R.string.update_activity_enable_unknown_sources_dialog_positive_btn, (dialog, which) -> {
							Intent allowUnknownAppsIntent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
							allowUnknownAppsIntent.setData(Uri.parse("package:" + getPackageName()));
							startActivity(allowUnknownAppsIntent);
						})
						.setCancelable(false)
						.show();
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

	private void showUpdateDialog() {
		// New update
		MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
		builder.setTitle(getString(R.string.update_dialog_title_new, appUpdate.getLatestVersion()));
		builder.setIcon(R.drawable.ic_system_update_24dp);
		builder.setMessage("What's new:\n" + appUpdate.getReleaseNotes());
		builder.setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> {
		});
		builder.setPositiveButton(R.string.update_dialog_positive_btn_text, (dialogInterface, i) -> {
			// Check if the device is running Android Marshmallow or higher
			// Marshmallow introduces the capability for runtime permissions
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				if (SharedUtils.Companion.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, UpdatesActivity.this)) {
					downloadUpdate(appUpdate.getUrlToDownload().toString(), appUpdate.getLatestVersion());
				} else {
					if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
						MaterialAlertDialogBuilder permBuilder = new MaterialAlertDialogBuilder(this);
						permBuilder.setTitle(R.string.update_perm_rationale_dialog_title)
								.setMessage(R.string.update_perm_rationale_dialog_msg)
								.setNegativeButton(R.string.update_perm_rationale_dialog_deny, (dialog, which) -> dialog.dismiss())
								.setPositiveButton(R.string.update_perm_rationale_dialog_grant, (dialog, which) -> requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0))
								.show();
					} else {
						requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
					}
				}
			} else {
				downloadUpdate(appUpdate.getUrlToDownload().toString(), appUpdate.getLatestVersion());
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
		builder.show();
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
						appUpdate = update;
						if (update.getLatestVersionCode() == BuildConfig.VERSION_CODE && !updateAvailable) {
							// User is running latest version
							Snackbar.make(findViewById(R.id.updatesView), R.string.update_snackbar_latest, Snackbar.LENGTH_SHORT)
									.show();
						} else {
							showUpdateDialog();
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
