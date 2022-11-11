package com.edricchan.studybuddy.exts.common

/**
 * The `TAG` constant. It can be used as the source of a message
 * as outputted in Android's Logcat.
 *
 * (Implementation from [StackOverflow](https://stackoverflow.com/a/53510106/6782707))
 */
val Any.TAG: String
    get() {
        val tag = javaClass.simpleName
        return if (tag.length <= 23) tag else tag.substring(0, 23)
    }
