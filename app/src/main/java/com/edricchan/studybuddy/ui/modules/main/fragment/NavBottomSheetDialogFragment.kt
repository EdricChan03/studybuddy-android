package com.edricchan.studybuddy.ui.modules.main.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.extensions.startActivity
import com.edricchan.studybuddy.ui.modules.account.AccountActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView

class NavBottomSheetDialogFragment : BottomSheetDialogFragment() {
	/** Listener for the [NavigationView] */
	var navigationViewListener: NavigationView.OnNavigationItemSelectedListener? = null
	/** Whether there is a logged in user */
	var isLoggedIn: Boolean = false
	var displayName: String? = null
	var email: String? = null
	var photoUrl: Uri? = null
	var navigationViewCheckedItemId: Int? = null
	private lateinit var navigationView: NavigationView
	/**
	 * Sets the listener for the [NavigationView]
	 *
	 * @param listener The listener
	 */
	@Deprecated("Use the navigationViewListener variable")
	fun setNavigationItemSelectedListener(listener: NavigationView.OnNavigationItemSelectedListener) {
		this.navigationViewListener = listener
	}


	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.frag_bottomappbar_bottomsheet, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		navigationView = view.findViewById(R.id.navigationView)
		if (navigationViewListener != null) {
			navigationView.setNavigationItemSelectedListener(navigationViewListener)
		}
		if (navigationViewCheckedItemId != null) {
			navigationView.setCheckedItem(navigationViewCheckedItemId!!)
		}

		if (savedInstanceState != null) {
			isLoggedIn = savedInstanceState.getBoolean(IS_LOGGED_IN_TAG)
			displayName = savedInstanceState.getString(USER_NAME_TAG)
			email = savedInstanceState.getString(USER_EMAIL_TAG)
			photoUrl = savedInstanceState.getString(USER_PHOTO_URL_TAG)?.toUri()
		}

		val headerView = navigationView.getHeaderView(0)

		headerView.findViewById<ImageButton>(R.id.userAccountSettings)
				.setOnClickListener {
					startActivity<AccountActivity>()
				}

		if (isLoggedIn) {
			if (displayName != null) headerView.findViewById<TextView>(R.id.userName).text = displayName
			if (email != null) headerView.findViewById<TextView>(R.id.userEmail).text = email
			if (photoUrl != null) {
				Glide.with(this)
						.load(photoUrl)
						.into(headerView.findViewById(R.id.userAvatar))
			}
		}
	}

	override fun onSaveInstanceState(outState: Bundle) {
		outState.putBoolean(IS_LOGGED_IN_TAG, isLoggedIn)
		outState.putString(USER_NAME_TAG, displayName)
		outState.putString(USER_EMAIL_TAG, email)
		outState.putString(USER_PHOTO_URL_TAG, photoUrl.toString())
		super.onSaveInstanceState(outState)
	}

	companion object {
		private const val IS_LOGGED_IN_TAG = "isLoggedIn"
		private const val USER_NAME_TAG = "userName"
		private const val USER_EMAIL_TAG = "userEmail"
		private const val USER_PHOTO_URL_TAG = "userPhotoUrl"
	}
}
