package com.edricchan.studybuddy.ui.modules.debug

import android.content.ComponentName
import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.MimeTypeConstants
import com.edricchan.studybuddy.ui.widget.bottomsheet.ModalBottomSheetAdapter
import com.edricchan.studybuddy.ui.widget.bottomsheet.ModalBottomSheetFragment
import com.edricchan.studybuddy.ui.widget.bottomsheet.interfaces.ModalBottomSheetItem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton


class DebugModalBottomSheetActivity : AppCompatActivity(R.layout.activity_debug_modal_bottom_sheet) {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		val modalBottomSheetLaunchersLayout = findViewById<LinearLayout>(R.id.modal_bottom_sheet_launcher_buttons_layout)

		addModalBottomSheetLauncher(
				modalBottomSheetLaunchersLayout,
				"Modal bottom sheet with text and no header",
				buildOnClickListener(modalBottomSheetWithTextNoHeader())
		)

		addModalBottomSheetLauncher(
				modalBottomSheetLaunchersLayout,
				"Modal bottom sheet with text and header",
				buildOnClickListener(modalBottomSheetWithTextAndHeader())
		)

		addModalBottomSheetLauncher(
				modalBottomSheetLaunchersLayout,
				"Modal bottom sheet with disabled even items",
				buildOnClickListener(modalBottomSheetDisabledEvenItems())
		)

		addModalBottomSheetLauncher(
				modalBottomSheetLaunchersLayout,
				"Modal bottom sheet with hidden odd items",
				buildOnClickListener(modalBottomSheetHiddenOddItems())
		)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		return if (item.itemId == android.R.id.home) {
			onBackPressed()
			true
		} else {
			super.onOptionsItemSelected(item)
		}
	}

	// Code adapted from
	// https://github.com/material-components/material-components-android/blob/master/catalog/java/io/material/catalog/picker/PickerMainDemoFragment.java
	private fun addModalBottomSheetLauncher(
			viewGroup: ViewGroup, @StringRes stringResId: Int, onClickListener: View.OnClickListener) {
		val bottomSheetLauncherButton = MaterialButton(viewGroup.context)
		bottomSheetLauncherButton.setOnClickListener(onClickListener)
		bottomSheetLauncherButton.setText(stringResId)
		viewGroup.addView(bottomSheetLauncherButton)
	}

	private fun addModalBottomSheetLauncher(
			viewGroup: ViewGroup, string: String, onClickListener: View.OnClickListener) {
		val bottomSheetLauncherButton = MaterialButton(viewGroup.context)
		bottomSheetLauncherButton.setOnClickListener(onClickListener)
		bottomSheetLauncherButton.text = string
		viewGroup.addView(bottomSheetLauncherButton)
	}

	// Code adapted from
	// https://github.com/material-components/material-components-android/blob/master/catalog/java/io/material/catalog/picker/PickerMainDemoFragment.java
	private fun buildOnClickListener(bottomSheetDialogFragment: BottomSheetDialogFragment): View.OnClickListener {
		return View.OnClickListener { bottomSheetDialogFragment.show(supportFragmentManager, bottomSheetDialogFragment.tag) }
	}

	private fun showToast(item: ModalBottomSheetItem) {
		showToast("Item ${item.itemId} clicked!")
	}

	private fun showToast(string: String) {
		Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
	}

	private fun modalBottomSheetWithTextNoHeader(): ModalBottomSheetFragment {
		val modalBottomSheetFragment = ModalBottomSheetFragment()
		for (i in 1..10) {
			modalBottomSheetFragment.addItem(ModalBottomSheetItem(itemId = i, title = "Item $i", onItemClickListener = object : ModalBottomSheetAdapter.OnItemClickListener {
				override fun onItemClick(item: ModalBottomSheetItem) {
					// Note: This is used to show that the onItemClick listener actually works by showing
					// the passing of the item data from the fragment to the adapter
					showToast(item)
				}
			}))
		}
		return modalBottomSheetFragment
	}

	private fun modalBottomSheetWithTextAndHeader(): ModalBottomSheetFragment {
		val modalBottomSheetFragment = ModalBottomSheetFragment()
		val headerTitle = getString(R.string.share_intent_value)
		val shareIntent = Intent().apply {
			action = Intent.ACTION_SEND
			putExtra(Intent.EXTRA_TEXT, R.string.share_content)
			type = MimeTypeConstants.textPlainMime
		}
		// See https://stackoverflow.com/a/9083910/6782707
		val shareIntentLaunchables = packageManager.queryIntentActivities(shareIntent, 0)
		shareIntentLaunchables?.sortWith(ResolveInfo.DisplayNameComparator(packageManager))
		shareIntentLaunchables?.forEach {
			modalBottomSheetFragment.addItem(ModalBottomSheetItem(
					title = it.loadLabel(packageManager).toString(),
					iconDrawable = it.loadIcon(packageManager),
					onItemClickListener = object : ModalBottomSheetAdapter.OnItemClickListener {
						override fun onItemClick(item: ModalBottomSheetItem) {
							showToast("Item ${item.title} clicked!")
							// Code adapted from
							// https://github.com/commonsguy/cw-advandroid/blob/master/Introspection/Launchalot/src/com/commonsware/android/launchalot/Launchalot.java
							val activity = it.activityInfo
							val componentName = ComponentName(activity.applicationInfo.packageName, activity.name)
							shareIntent.component = componentName
							startActivity(shareIntent)

							// Dismiss the bottom sheet
							modalBottomSheetFragment.dismiss()
						}
					}
			))
		}
		modalBottomSheetFragment.headerTitle = headerTitle
		return modalBottomSheetFragment
	}

	private fun modalBottomSheetDisabledEvenItems(): ModalBottomSheetFragment {
		val modalBottomSheetFragment = ModalBottomSheetFragment()
		for (i in 1..10) {
			// Only enable on even items
			val enabled = i % 2 == 0
			modalBottomSheetFragment.addItem(ModalBottomSheetItem(
					itemId = i,
					title = "Item $i",
					enabled = enabled,
					onItemClickListener = object : ModalBottomSheetAdapter.OnItemClickListener {
						override fun onItemClick(item: ModalBottomSheetItem) {
							showToast(item)
						}

					}
			))
		}
		return modalBottomSheetFragment
	}

	private fun modalBottomSheetHiddenOddItems(): ModalBottomSheetFragment {
		val modalBottomSheetFragment = ModalBottomSheetFragment()
		for (i in 1..10) {
			// Only hide on odd items
			// This indicates that all even items are visible
			val visible = i % 2 == 0
			modalBottomSheetFragment.addItem(ModalBottomSheetItem(
					itemId = i,
					title = "Item $i",
					visible = visible,
					onItemClickListener = object : ModalBottomSheetAdapter.OnItemClickListener {
						override fun onItemClick(item: ModalBottomSheetItem) {
							showToast(item)
						}
					}
			))
		}
		return modalBottomSheetFragment
	}
}