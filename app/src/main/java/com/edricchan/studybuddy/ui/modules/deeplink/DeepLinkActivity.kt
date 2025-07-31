package com.edricchan.studybuddy.ui.modules.deeplink

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.deeplinkdispatch.DeepLinkHandler

@DeepLinkHandler(DeepLinkModule::class)
class DeepLinkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DeepLinkDelegate(DeepLinkModuleRegistry()).dispatchFrom(this)
        finish()
    }
}
