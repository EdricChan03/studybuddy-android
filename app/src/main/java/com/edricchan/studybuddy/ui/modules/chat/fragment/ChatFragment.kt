package com.edricchan.studybuddy.ui.modules.chat.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.ContentView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.ui.modules.chat.NewChatActivity
import com.edricchan.studybuddy.utils.SharedUtils

@ContentView(R.layout.frag_chat)
class ChatFragment : Fragment() {
	private lateinit var fragmentView: View
	private lateinit var mParentActivity: AppCompatActivity

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		fragmentView = view
		SharedUtils.setBottomAppBarFabOnClickListener(mParentActivity, View.OnClickListener {
			startActivity(Intent(context, NewChatActivity::class.java))
		})
	}

	override fun onAttach(context: Context) {
		mParentActivity = context as AppCompatActivity
		super.onAttach(context as Context)
	}

	/*private fun newChatDialog() {
		val newChatDialogView = layoutInflater.inflate(R.layout.new_chat_dialog, null)
		context?.let {
			MaterialAlertDialogBuilder(it)
					.setTitle(R.string.chat_frag_new_chat_dialog_title)
					.setView(newChatDialogView)
					.setNegativeButton(R.string.dialog_action_cancel) { dialog, which ->
						dialog.dismiss()
					}
					.setPositiveButton(R.string.dialog_action_create) { dialog, which ->
						// TODO: Add support to create new chat
						Log.d(TAG, "Creating new chat...")
					}
					.show()
		}
	}*/

	companion object {
		/**
		 * The Android tag for use with [android.util.Log]
		 */
		private val TAG = SharedUtils.getTag(this::class.java)
	}
}
