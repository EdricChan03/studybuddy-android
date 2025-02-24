package com.edricchan.studybuddy.core.deeplink

import com.airbnb.deeplinkdispatch.DeepLinkSpec

// We need to duplicate this as annotation arguments only support
// compile-time constants
/** URLs to be prefixed to deep-link app URIs. */
val AppPrefixUrls = listOf(
    "app://studybuddy",
    "studybuddy://"
)

@DeepLinkSpec(
    prefix = [
        "app://studybuddy",
        "studybuddy://"
    ]
)
@Target(AnnotationTarget.CLASS)
annotation class AppDeepLink(val value: Array<String>)
