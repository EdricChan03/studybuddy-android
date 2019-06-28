package com.edricchan.studybuddy.annotations

import com.airbnb.deeplinkdispatch.DeepLinkSpec

@DeepLinkSpec(prefix = [
	"http://studybuddy-e5f46.firebaseapp.com",
	"https://studybuddy-e5f46.firebaseapp.com",
	"http://studybuddy-e5f46.web.app",
	"https://studybuddy-e5f46.web.app"
])
@Target(AnnotationTarget.CLASS)
annotation class WebDeepLink(val value: Array<String>)