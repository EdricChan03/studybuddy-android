package com.edricchan.studybuddy.annotations

import com.airbnb.deeplinkdispatch.DeepLinkSpec

@DeepLinkSpec(prefix = ["app://studybuddy"])
@Target(AnnotationTarget.CLASS)
annotation class AppDeepLink(val value: Array<String>)