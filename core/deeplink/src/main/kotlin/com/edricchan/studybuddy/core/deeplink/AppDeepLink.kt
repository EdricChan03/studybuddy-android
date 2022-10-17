package com.edricchan.studybuddy.core.deeplink

import com.airbnb.deeplinkdispatch.DeepLinkSpec

@DeepLinkSpec(prefix = ["app://studybuddy"])
@Target(AnnotationTarget.CLASS)
annotation class AppDeepLink(val value: Array<String>)
