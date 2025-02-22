package com.edricchan.studybuddy.features.tasks.ui.utils

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

/**
 * Converts the [String] receiver to its [Color] equivalent, as specified by [toColorInt].
 * @see toColorInt
 */
internal val String.composeColor: Color
    get() = Color(toColorInt())

/** Sample text to test Markdown rendering. */
internal val SampleMarkdownText: String by lazy {
    """
    Finish the Compose rewrite for the tasks feature :tada:
    ---
    **Bold** *Italics* ~~Strike-through~~ `code`
    > Quote
    ```kotlin
    val isKotlin = true
    ```
    ![Random picture](https://picsum.photos/200/300)

    [Link](https://example.com)
    * Bullet list
    * Another list item

    1. Item 1
    2. Item 2
    3. Item 3

    * [ ] Check-list item 1
    * [x] Checked item 2
    """.trimIndent()
}
