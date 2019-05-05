package com.edricchan.studybuddy.annotations

import com.airbnb.deeplinkdispatch.DeepLinkSpec

@DeepLinkSpec(prefix = ["http://studybuddy-e5f46.firebaseapp.com", "https://studybuddy-e5f46.firebaseapp.com"])
@Target(AnnotationTarget.CLASS)
annotation class WebDeepLink(val value: Array<String>)