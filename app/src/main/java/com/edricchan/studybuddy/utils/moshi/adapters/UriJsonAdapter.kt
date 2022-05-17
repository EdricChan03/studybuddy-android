@file:Suppress("unused")

package com.edricchan.studybuddy.utils.moshi.adapters

import android.net.Uri
import androidx.core.net.toUri
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

/** Moshi adapter to convert a [Uri] to and from its JSON representation. */
object UriJsonAdapter {
    @ToJson fun toJson(uri: Uri) = uri.toString()

    @FromJson fun fromJson(uri: String) = uri.toUri()
}
