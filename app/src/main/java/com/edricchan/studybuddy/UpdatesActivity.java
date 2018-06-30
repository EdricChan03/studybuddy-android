package com.edricchan.studybuddy;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.javiersantos.appupdater.AppUpdaterUtils;
import com.github.javiersantos.appupdater.enums.AppUpdaterError;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.github.javiersantos.appupdater.objects.Update;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class UpdatesActivity extends AppCompatActivity {

	boolean isChecking = false;
	MaterialButton checkForUpdatesBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_updates);
		checkForUpdatesBtn = (MaterialButton) findViewById(R.id.empty_view_cta);
		checkForUpdatesBtn.setOnClickListener(click -> checkForUpdates());
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
		final DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
		request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, getString(R.string.download_apk_name, version));
		final DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
		assert manager != null;
		final long id = manager.enqueue(request);
		//set BroadcastReceiver to install app when .apk is downloaded
		BroadcastReceiver onComplete = new BroadcastReceiver() {
			public void onReceive(Context context1, Intent intent) {
				Intent install = new Intent(Intent.ACTION_VIEW);
				install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				install.setDataAndType(manager.getUriForDownloadedFile(id), "application/vnd.android.package-archive");
				startActivity(install);

				unregisterReceiver(this);
			}
		};
		getApplicationContext().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

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
							builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialogInterface, int i) {

								}
							});
							builder.setPositiveButton(R.string.update_dialog_positive_btn_text, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialogInterface, int i) {
									if (SharedHelper.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, UpdatesActivity.this)) {
										downloadUpdate(update.getUrlToDownload().toString(), update.getLatestVersion());
									}
								}
							});
							builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
								@Override
								public void onDismiss(DialogInterface dialogInterface) {
									isChecking = false;
									invalidateOptionsMenu();
								}
							});
							builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
								@Override
								public void onCancel(DialogInterface dialogInterface) {
									isChecking = false;
									invalidateOptionsMenu();
								}
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
										.setAction(R.string.dialog_action_retry, new View.OnClickListener() {
											@Override
											public void onClick(View view) {
												checkForUpdates();
											}
										})
										.show();
								break;
							case JSON_ERROR:
								Snackbar.make(findViewById(R.id.updatesView), R.string.update_snackbar_error_malformed, Snackbar.LENGTH_LONG)
										.setAction(R.string.dialog_action_retry, new View.OnClickListener() {
											@Override
											public void onClick(View view) {
												checkForUpdates();
											}
										})
										.show();
								break;
							default:
								Snackbar.make(findViewById(R.id.updatesView), R.string.update_snackbar_error, Snackbar.LENGTH_LONG)
										.setAction(R.string.dialog_action_retry, new View.OnClickListener() {
											@Override
											public void onClick(View view) {
												checkForUpdates();
											}
										})
										.show();
								break;
						}
					}
				});
		appUpdaterUtils.start();
	}
}
