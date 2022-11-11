package com.edricchan.studybuddy.exts.common

import kotlin.test.Test
import kotlin.test.assertEquals

internal class LogExtsTest {
    @Test
    fun getTag() {
        val tag = TAG
        val expectedTag = LogExtsTest::class.java.simpleName
        assertEquals(tag, expectedTag)
        assertEquals(tag.length, expectedTag.length)
    }

    @Test
    fun truncatesTag() {
        val tag = MyClassWithAVeryLongNameThatExceedsTheLimit.TAG
        val truncatedLength = 23
        val expectedTag =
            MyClassWithAVeryLongNameThatExceedsTheLimit::class.java.simpleName
                .substring(0, truncatedLength)
        assertEquals(tag, expectedTag)
        assertEquals(tag.length, truncatedLength)
    }

    internal object MyClassWithAVeryLongNameThatExceedsTheLimit
}
