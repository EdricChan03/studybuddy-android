package com.edricchan.studybuddy.core.deeplink

import com.airbnb.deeplinkdispatch.DeepLinkSpec

// We need to duplicate this as annotation arguments only support
// compile-time constants
/** URLs to be prefixed to deep-link HTTP/HTTPS URIs. */
val WebPrefixUrls = listOf(
    "http://studybuddy-e5f46.firebaseapp.com",
    "https://studybuddy-e5f46.firebaseapp.com",
    "http://studybuddy-e5f46.web.app",
    "https://studybuddy-e5f46.web.app"
)

@DeepLinkSpec(
    prefix = [
        "http://studybuddy-e5f46.firebaseapp.com",
        "https://studybuddy-e5f46.firebaseapp.com",
        "http://studybuddy-e5f46.web.app",
        "https://studybuddy-e5f46.web.app"
    ]
)
@Target(AnnotationTarget.CLASS)
annotation class WebDeepLink(val value: Array<String>)
