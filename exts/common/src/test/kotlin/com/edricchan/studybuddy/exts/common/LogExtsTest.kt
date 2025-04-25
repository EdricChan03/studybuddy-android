package com.edricchan.studybuddy.exts.common

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldHaveSameLengthAs

internal object MyClassWithAVeryLongNameThatExceedsTheLimit

internal class LogTestClass
internal object LogTestObject

class LogExtsTest : DescribeSpec({
    describe("TAG") {
        it("should get the tag of a class") {
            val tag = LogTestClass().TAG
            val expectedTag = LogTestClass::class.java.simpleName
            tag shouldBe expectedTag
            tag shouldHaveSameLengthAs expectedTag
        }

        it("should get the tag of an object") {
            val tag = LogTestObject.TAG
            val expectedTag = LogTestObject::class.java.simpleName
            tag shouldBe expectedTag
            tag shouldHaveSameLengthAs expectedTag
        }

        it("should truncate long class names") {
            val tag = MyClassWithAVeryLongNameThatExceedsTheLimit.TAG
            val truncatedLength = 23
            val expectedTag =
                MyClassWithAVeryLongNameThatExceedsTheLimit::class.java.simpleName
                    .substring(0, truncatedLength)
            tag shouldBe expectedTag
            tag shouldHaveSameLengthAs expectedTag
        }
    }
})
