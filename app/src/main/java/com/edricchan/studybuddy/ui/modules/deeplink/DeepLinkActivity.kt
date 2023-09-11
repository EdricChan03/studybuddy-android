package com.edricchan.studybuddy.ui.modules.deeplink

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.deeplinkdispatch.DeepLinkHandler
import com.edricchan.studybuddy.exts.common.TAG
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase

@DeepLinkHandler(DeepLinkModule::class)
class DeepLinkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.data?.host == "studybuddy.page.link") {
            // The intent is from the Dynamic Link host
            Firebase.dynamicLinks
                .getDynamicLink(intent)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        /*Toast.makeText(this, "Link received: ${it.result?.link}", Toast.LENGTH_LONG)
                                .show()*/
                        Log.d(TAG, "Deep link received: ${it.result?.link}")
                        // Overwrite intent's data with new Uri
                        intent.data = it.result?.link
                    } else {
                        Log.e(
                            TAG,
                            "An error occurred while attempting to retrieve the dynamic link:",
                            it.exception
                        )
                    }
                }
        }
        DeepLinkDelegate(DeepLinkModuleRegistry()).dispatchFrom(this)
        finish()
    }
}
