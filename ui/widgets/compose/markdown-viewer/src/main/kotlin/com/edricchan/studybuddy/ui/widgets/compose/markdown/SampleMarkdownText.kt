package com.edricchan.studybuddy.ui.widgets.compose.markdown

import androidx.annotation.VisibleForTesting

/** Sample text to test Markdown rendering. */
@VisibleForTesting
val SampleMarkdownText: String by lazy {
    """
    Finish the Compose rewrite for the tasks feature :tada:

    ---

    # Heading 1
    ## Heading 2
    ### Heading 3
    #### Heading 4
    ##### Heading 5
    ###### Heading 6

    ---

    **Bold** *Italics* ~~Strike-through~~ `code`
    > Quote
    ```kotlin
    val isKotlin = true
    ```

    ---

    ![Random picture](https://picsum.photos/200/300)

    [Link](https://example.com)

    ---

    * Bullet list
    * Another list item

    1. Item 1
    2. Item 2
    3. Item 3

    ---

    Col 1 | Col 2 | Col 3 | Col 4 | Col 5
    ---|---|---|---|---
    Value 1 | Value 2 | Value 3 | Value 4 | Value 5
    Value 6 | Value 7 | Value 8 | Value 9 | Value 10
    Value 11 | Value 12 | Value 13 | Value 14 | Value 15
    Value 16 | Value 17 | Value 18 | Value 19 | Value 20
    Value 21 | Value 22 | Value 23 | Value 24 | Value 25
    """.trimIndent()
}
