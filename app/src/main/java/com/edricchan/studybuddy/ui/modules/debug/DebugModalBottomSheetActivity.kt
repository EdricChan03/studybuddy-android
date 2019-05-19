package com.edricchan.studybuddy.ui.modules.debug

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.edricchan.studybuddy.R
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
		var items: MutableList<ModalBottomSheetItem> = mutableListOf()
		for (i in 1..10) {
			items.add(ModalBottomSheetItem(title = "Item $i", onClickListener = View.OnClickListener {
				Toast.makeText(this, "Item $i clicked!", Toast.LENGTH_SHORT).show()
			}))
		}
		modalBottomSheetFragment.setItems(items.toTypedArray())
		return modalBottomSheetFragment
	}
}