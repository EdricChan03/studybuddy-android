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
import com.edricchan.studybuddy.ui.widget.bottomsheet.interfaces.ModalBottomSheetGroup
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
				"Modal bottom sheet with text items and no header",
				buildOnClickListener(modalBottomSheetWithTextNoHeader())
		)

		addModalBottomSheetLauncher(
				modalBottomSheetLaunchersLayout,
				"Modal bottom sheet with text items and header",
				buildOnClickListener(modalBottomSheetWithTextAndHeader())
		)

		addModalBottomSheetLauncher(
				modalBottomSheetLaunchersLayout,
				"Modal bottom sheet with icons and text items",
				buildOnClickListener(modalBottomSheetWithIconItems())
		)

		addModalBottomSheetLauncher(
				modalBottomSheetLaunchersLayout,
				"Modal bottom sheet with 1000 text items",
				buildOnClickListener(modalBottomSheetWith1000Items())
		)

		addModalBottomSheetLauncher(
				modalBottomSheetLaunchersLayout,
				"Modal bottom sheet with text items with checkboxes",
				buildOnClickListener(modalBottomSheetCheckBoxItems())
		)

		addModalBottomSheetLauncher(
				modalBottomSheetLaunchersLayout,
				"Modal bottom sheet with text items with radio buttons",
				buildOnClickListener(modalBottomSheetRadioButtonItems())
		)

		addModalBottomSheetLauncher(
				modalBottomSheetLaunchersLayout,
				"Modal bottom sheet with disabled even text items",
				buildOnClickListener(modalBottomSheetDisabledEvenItems())
		)

		addModalBottomSheetLauncher(
				modalBottomSheetLaunchersLayout,
				"Modal bottom sheet with hidden odd text items",
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
		showToast("Item ${item.id} clicked!")
	}

	private fun showToast(string: String) {
		Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
	}

	private fun modalBottomSheetWithTextNoHeader(): ModalBottomSheetFragment {
		val modalBottomSheetFragment = ModalBottomSheetFragment()
		for (i in 1..10) {
			modalBottomSheetFragment.addItem(ModalBottomSheetItem(id = i, title = "Item $i",
					onItemClickListener = object : ModalBottomSheetAdapter.OnItemClickListener {
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
		val shareIntent = Intent(Intent.ACTION_SEND).apply {
			putExtra(Intent.EXTRA_TEXT, R.string.share_content)
			type = MimeTypeConstants.textPlainMime
		}
		// See https://stackoverflow.com/a/9083910/6782707
		val shareIntentLaunchables = packageManager.queryIntentActivities(shareIntent, 0)
		shareIntentLaunchables.sortWith(ResolveInfo.DisplayNameComparator(packageManager))
		shareIntentLaunchables.forEach { resolveInfo ->
			modalBottomSheetFragment.addItem(ModalBottomSheetItem(
					title = resolveInfo.loadLabel(packageManager).toString(),
					iconDrawable = resolveInfo.loadIcon(packageManager),
					onItemClickListener = object : ModalBottomSheetAdapter.OnItemClickListener {
						override fun onItemClick(item: ModalBottomSheetItem) {
							showToast("Item ${item.title} clicked!")
							// Code adapted from
							// https://github.com/commonsguy/cw-advandroid/blob/master/Introspection/Launchalot/src/com/commonsware/android/launchalot/Launchalot.java
							val activity = resolveInfo.activityInfo
							val intent = Intent(shareIntent).apply {
								component = ComponentName(activity.packageName, activity.name)
								flags = Intent.FLAG_ACTIVITY_NEW_TASK
							}
							startActivity(intent)

							// Dismiss the bottom sheet
							modalBottomSheetFragment.dismiss()
						}
					}
			))
		}
		modalBottomSheetFragment.headerTitle = headerTitle
		return modalBottomSheetFragment
	}
	
	private fun modalBottomSheetWith1000Items(): ModalBottomSheetFragment {
		val modalBottomSheetFragment = ModalBottomSheetFragment()
		for (i in 1..1000) {
			modalBottomSheetFragment.addItem(ModalBottomSheetItem(
					id = i,
					title = "Item $i",
					onItemClickListener = object : ModalBottomSheetAdapter.OnItemClickListener {
						override fun onItemClick(item: ModalBottomSheetItem) {
							showToast(item)
						}

					}
			))
		}
		return modalBottomSheetFragment
	}

	private fun modalBottomSheetWithIconItems(): ModalBottomSheetFragment {
		val modalBottomSheetFragment = ModalBottomSheetFragment()
		val onItemClickListener = object : ModalBottomSheetAdapter.OnItemClickListener {
			override fun onItemClick(item: ModalBottomSheetItem) {
				showToast("Item ${item.title} clicked!")
			}

		}
		modalBottomSheetFragment.items = mutableListOf(
				ModalBottomSheetItem(id = 1, title = "About app", icon = R.drawable.ic_info_outline_24dp,
						onItemClickListener = onItemClickListener),
				ModalBottomSheetItem(id = 2, title = "Share this app", icon = R.drawable.ic_share_outline_24dp,
						onItemClickListener = onItemClickListener),
				ModalBottomSheetItem(id = 3, title = "Account", icon = R.drawable.ic_account_circle_outline_24dp,
						onItemClickListener = onItemClickListener),
				ModalBottomSheetItem(id = 4, title = "Settings", icon = R.drawable.ic_settings_outline_24dp,
						onItemClickListener = onItemClickListener),
				ModalBottomSheetItem(id = 5, title = "Help", icon = R.drawable.ic_help_outline_24dp,
						onItemClickListener = onItemClickListener)
		)
		return modalBottomSheetFragment
	}

	private fun modalBottomSheetCheckBoxItems(): ModalBottomSheetFragment {
		val modalBottomSheetFragment = ModalBottomSheetFragment()
		val group = ModalBottomSheetGroup(
				id = 1000,
				checkableBehavior = ModalBottomSheetGroup.CHECKABLE_BEHAVIOR_ALL,
				onItemCheckedChangeListener = object : ModalBottomSheetAdapter.OnItemCheckedChangeListener {
					override fun onItemCheckedChange(item: ModalBottomSheetItem) {
						showToast(item)
					}
				}
		)
		for (i in 1..10) {
			modalBottomSheetFragment.addItem(ModalBottomSheetItem(
					id = i,
					title = "Item $i",
					group = group
			))
		}
		return modalBottomSheetFragment
	}

	private fun modalBottomSheetRadioButtonItems(): ModalBottomSheetFragment {
		val modalBottomSheetFragment = ModalBottomSheetFragment()
		val groupId = 1000
		val group = ModalBottomSheetGroup(
				id = groupId,
				checkableBehavior = ModalBottomSheetGroup.CHECKABLE_BEHAVIOR_SINGLE,
				onItemCheckedChangeListener = object : ModalBottomSheetAdapter.OnItemCheckedChangeListener {
					override fun onItemCheckedChange(item: ModalBottomSheetItem) {
						showToast(item)
					}
				}
		)
		for (i in 1..10) {
			modalBottomSheetFragment.addItem(ModalBottomSheetItem(
					id = i,
					title = "Item $i",
					group = group
			))
		}
		return modalBottomSheetFragment
	}

	private fun modalBottomSheetDisabledEvenItems(): ModalBottomSheetFragment {
		val modalBottomSheetFragment = ModalBottomSheetFragment()
		for (i in 1..10) {
			// Only disable on even items
			val disabled = i % 2 == 0
			modalBottomSheetFragment.addItem(ModalBottomSheetItem(
					id = i,
					title = "Item $i",
					enabled = !disabled,
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
					id = i,
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