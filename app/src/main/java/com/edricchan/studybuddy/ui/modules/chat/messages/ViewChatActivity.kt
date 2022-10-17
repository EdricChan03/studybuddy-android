package com.edricchan.studybuddy.ui.modules.chat.messages

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.airbnb.deeplinkdispatch.DeepLink
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.core.deeplink.AppDeepLink
import com.edricchan.studybuddy.core.deeplink.WebDeepLink
import com.edricchan.studybuddy.extensions.TAG
import com.edricchan.studybuddy.ui.common.BaseActivity

@WebDeepLink(["/chat/{id}", "/chats/view/{id}"])
@AppDeepLink(["/chat/{id}", "/chats/view/{id}"])
class ViewChatActivity : BaseActivity(R.layout.activity_view_chat) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
            Log.d(TAG, "Chat ID (deep link): ${intent.extras?.getString("id")}")
        } else if (intent != null && intent.extras != null && intent.extras?.containsKey(KEY_CHAT_ID) == true) {
            Log.d(TAG, "Chat ID: ${intent.extras?.get(KEY_CHAT_ID)}")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_view_chat, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val KEY_CHAT_ID = "CHAT_ID"
    }
}
