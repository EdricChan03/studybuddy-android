package com.edricchan.studybuddy.utils.moshi.adapters

import android.net.Uri
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

object UriJsonAdapter {
    @ToJson fun toJson(uri: Uri) = uri.toString()

    @FromJson fun fromJson(uri: String) = Uri.parse(uri)
}
