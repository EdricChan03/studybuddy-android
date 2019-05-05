package com.edricchan.studybuddy.ui.modules.deeplink

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.deeplinkdispatch.DeepLinkHandler
import com.edricchan.studybuddy.utils.SharedUtils
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks

@DeepLinkHandler(DeepLinkModule::class)
class DeepLinkActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		if (intent.data?.host == "studybuddy.page.link") {
			// The intent is from the Dynamic Link host
			FirebaseDynamicLinks.getInstance()
					.getDynamicLink(intent)
					.addOnCompleteListener {
						if (it.isSuccessful) {
							/*Toast.makeText(this, "Link received: ${it.result?.link}", Toast.LENGTH_LONG)
									.show()*/
							Log.d(TAG, "Deep link received: ${it.result?.link}")
							// Overwrite intent's data with new Uri
							intent.data = it.result?.link
						} else {
							Log.e(TAG, "An error occurred while attempting to retrieve the dynamic link:", it.exception)
						}
					}
		}
		DeepLinkDelegate(DeepLinkModuleLoader()).dispatchFrom(this)
		finish()
	}

	companion object {
		private val TAG = SharedUtils.getTag(DeepLinkActivity::class.java)
	}
}