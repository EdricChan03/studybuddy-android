package com.edricchan.studybuddy.receiver;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;

import com.edricchan.studybuddy.R;
import com.edricchan.studybuddy.SharedHelper;

import java.io.File;

public class ActionButtonReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(final Context context, Intent intent) {
		String TAG = "ActionButtonReceiver";
		String action = intent.getStringExtra("action");
		switch (action) {
			case SharedHelper.ACTION_NOTIFICATIONS_START_DOWNLOAD_RECEIVER:
				checkPermission(context, intent);
				//register receiver for when .apk download is compete
				break;
			case SharedHelper.ACTION_NOTIFICATIONS_RETRY_CHECK_FOR_UPDATE_RECEIVER:
				SharedHelper.checkForUpdates(context);
				break;
		}
	}

	public void checkPermission(Context context, Intent intent) {
		if (SharedHelper.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, context)) {
			downloadUpdate(context, intent);
		}
	}

	public void downloadUpdate(final Context context, Intent intent) {
		final DownloadManager.Request request = new DownloadManager.Request(Uri.parse(intent.getStringExtra("downloadUrl")));
		request.setTitle(context.getString(R.string.notification_downloading_update_title, intent.getStringExtra("version")));
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, context.getString(R.string.download_apk_name, intent.getStringExtra("version")));
		final DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
		long id = manager.enqueue(request);
		//set BroadcastReceiver to install app when .apk is downloaded
		BroadcastReceiver onComplete = new BroadcastReceiver() {
			public void onReceive(Context ctxt, Intent intent) {
				Intent install = new Intent(Intent.ACTION_INSTALL_PACKAGE);
				install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				install.setData(Uri.fromFile(new File(Environment.DIRECTORY_DOWNLOADS, context.getString(R.string.download_apk_name, intent.getStringExtra("version")))));
				context.startActivity(install);

				context.unregisterReceiver(this);
			}
		};
		context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

	}
}
