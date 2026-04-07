package com.edricchan.studybuddy.features.tasks.data.model

import com.edricchan.studybuddy.data.common.Color
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.string.shouldBeEqualIgnoringCase
import io.kotest.property.Arb
import io.kotest.property.arbitrary.Codepoint
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.hex
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

fun Arb.Companion.hexColor() = arbitrary {
    "#${Arb.string(size = 6, codepoints = Codepoint.hex()).bind()}"
}

@Suppress("DEPRECATION") // For deprecated color property
class TodoProjectBuilderTest : DescribeSpec({
    describe("TodoProject") {
        describe("Builder") {
            describe("color property") {
                it("should delegate to colorValue") {
                    checkAll(Arb.hexColor()) {
                        with(TodoProject.Builder()) {
                            val colorData = Color(it)
                            color = it
                            colorValue.shouldNotBeNull {
                                this shouldBeEqual colorData
                            }
                            color.shouldNotBeNull {
                                this shouldBeEqual colorData.toRgbHexString()
                            }
                        }
                    }
                }

                it("should set both color and colorInt on the TodoProject data") {
                    checkAll(Arb.hexColor()) { hexColor ->
                        TodoProject.build {
                            color = hexColor
                        }.should {
                            it.color shouldNotBeNull {
                                this shouldBeEqualIgnoringCase hexColor
                            }
                            it.colorInt shouldNotBeNull {
                                this shouldBeEqual Color(hexColor).value
                            }
                        }
                    }
                }
            }
        }
    }
})
