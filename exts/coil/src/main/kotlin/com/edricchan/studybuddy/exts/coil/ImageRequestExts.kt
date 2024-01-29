package com.edricchan.studybuddy.exts.coil

import android.content.Context
import coil.request.ImageRequest

// From https://github.com/coil-kt/coil/issues/674

/** Creates a new [ImageRequest] given the configuration [block]. */
inline fun Context.imageRequest(
    block: ImageRequest.Builder.() -> Unit
) = ImageRequest.Builder(this).apply(block).build()
