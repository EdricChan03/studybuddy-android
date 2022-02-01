package com.edricchan.studybuddy.ui.modules.main.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import coil.load
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.extensions.startActivity
import com.edricchan.studybuddy.ui.modules.account.AccountActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView

class NavBottomSheetDialogFragment : BottomSheetDialogFragment() {
    /** Listener for the [NavigationView] */
    var navigationViewListener: (menuItem: MenuItem) -> Boolean = {false}
    /** Whether there is a logged in user */
    var isLoggedIn: Boolean = false
    var displayName: String? = null
    var email: String? = null
    var photoUrl: Uri? = null
    var navigationViewCheckedItemId: Int? = null
    private lateinit var navigationView: NavigationView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frag_bottomappbar_bottomsheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigationView = view.findViewById(R.id.navigationView)
        navigationView.setNavigationItemSelectedListener(navigationViewListener)
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

        headerView.apply {
            setOnClickListener {
                startActivity<AccountActivity>()
            }

            if (isLoggedIn) {
                if (displayName != null) findViewById<TextView>(R.id.userName).text = displayName
                if (email != null) findViewById<TextView>(R.id.userEmail).text = email
                if (photoUrl != null) findViewById<ImageView>(R.id.userAvatar).load(photoUrl)
            }
        } else {
            headerView.findViewById<TextView>(R.id.userName).text = getString(
                R.string.account_user_name_default
            )
            headerView.findViewById<TextView>(R.id.userEmail).text = getString(
                R.string.account_user_email_default
            )
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
