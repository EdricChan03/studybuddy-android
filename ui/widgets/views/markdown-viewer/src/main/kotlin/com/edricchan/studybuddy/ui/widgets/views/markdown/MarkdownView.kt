package com.edricchan.studybuddy.ui.widgets.views.markdown

import android.content.Context
import android.content.res.TypedArray
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.annotation.StyleableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.unit.dp
import androidx.core.content.withStyledAttributes
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme
import com.edricchan.studybuddy.ui.widgets.compose.markdown.MarkdownViewer
import com.edricchan.studybuddy.ui.widgets.compose.markdown.SampleMarkdownText
import kotlinx.parcelize.Parcelize

/** [android.view.View] to render Markdown using the [MarkdownViewer] composable. */
@Deprecated("Use the MarkdownViewer composable instead")
class MarkdownView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AbstractComposeView(
    context = context,
    attrs = attrs,
    defStyleAttr = defStyleAttr
) {
    /**
     * Desired Markdown text to be rendered.
     * @see R.styleable.MarkdownView_android_text
     */
    var markdownText by mutableStateOf("")

    /**
     * Sets the desired Markdown text to be rendered with the given [textRes] string
     * resource.
     */
    fun setMarkdownText(@StringRes textRes: Int) {
        markdownText = context.getString(textRes)
    }

    /**
     * Whether the text contents of this view is selectable by the user.
     * @see R.styleable.MarkdownView_android_textIsSelectable
     */
    var isTextSelectable by mutableStateOf(false)

    /**
     * Padding to be applied to the [MarkdownViewer] composable.
     * @see R.styleable.MarkdownView_markdown_contentPadding
     * @see R.styleable.MarkdownView_markdown_contentPaddingBottom
     * @see R.styleable.MarkdownView_markdown_contentPaddingEnd
     * @see R.styleable.MarkdownView_markdown_contentPaddingStart
     * @see R.styleable.MarkdownView_markdown_contentPaddingTop
     */
    var contentPadding by mutableStateOf(PaddingValues(16.dp))

    /**
     * Colours to be used for the [MarkdownViewer].
     * @property containerColor Desired container (background) colour as a colour integer.
     * @property contentColor Desired content (text) colour as a colour integer.
     * @property linkContentColor Desired link content (text) colour as a colour integer.
     */
    @Immutable
    data class ColorSpec(
        @field:ColorInt
        val containerColor: Int? = null,
        @field:ColorInt
        val contentColor: Int? = null,
        @field:ColorInt
        val linkContentColor: Int? = null
    ) {
        /**
         * Gets the container (background) colour to use a Compose [Color], or
         * [androidx.compose.material3.ColorScheme.surface] if `null` is specified for
         * [containerColor].
         */
        @Composable
        fun container(): Color =
            containerColor?.let(::Color) ?: MaterialTheme.colorScheme.surface

        /**
         * Gets the content (text) colour to use a Compose [Color], or
         * the result of [contentColorFor] with [container]'s return value if `null` is
         * specified for [contentColor].
         */
        @Composable
        fun content(): Color =
            contentColor?.let(::Color) ?: contentColorFor(container())

        /**
         * Gets the link content (text) colour to use a Compose [Color], or
         * [androidx.compose.material3.ColorScheme.primary] if `null` is specified for
         * [linkContentColor].
         */
        @Composable
        fun linkContent(): Color =
            linkContentColor?.let(::Color) ?: MaterialTheme.colorScheme.primary
    }

    /**
     * Desired colours to be used for this composable.
     */
    var colorSpec: ColorSpec by mutableStateOf(ColorSpec())

    /**
     * Sets the container (background) colour to be used as a colour integer.
     * @see R.styleable.MarkdownView_markdown_containerColor
     */
    fun setContainerColor(@ColorInt color: Int) {
        colorSpec = colorSpec.copy(containerColor = color)
    }

    /**
     * Sets the container (background) colour to be used as a colour resource.
     * @see R.styleable.MarkdownView_markdown_containerColor
     */
    fun setContainerColorResource(@ColorRes colorRes: Int) {
        setContainerColor(context.getColor(colorRes))
    }

    /**
     * Sets the content (text) colour to be used as a colour integer.
     * @see R.styleable.MarkdownView_markdown_containerColor
     */
    fun setContentColor(@ColorInt color: Int) {
        colorSpec = colorSpec.copy(contentColor = color)
    }

    /**
     * Sets the content (text) colour to be used as a colour resource.
     * @see R.styleable.MarkdownView_markdown_containerColor
     */
    fun setContentColorResource(@ColorRes colorRes: Int) {
        setContentColor(context.getColor(colorRes))
    }

    /**
     * Sets the link content (text) colour to be used as a colour integer.
     * @see R.styleable.MarkdownView_markdown_containerColor
     */
    fun setLinkContentColor(@ColorInt color: Int) {
        colorSpec = colorSpec.copy(linkContentColor = color)
    }

    /**
     * Sets the link content (text) colour to be used as a colour resource.
     * @see R.styleable.MarkdownView_markdown_containerColor
     */
    fun setLinkContentColorResource(@ColorRes colorRes: Int) {
        setLinkContentColor(context.getColor(colorRes))
    }

    /**
     * Sets the desired container, content and link content colours to be used
     * from the given colour integers.
     * @see setContainerColor
     * @see setContentColor
     * @see setLinkContentColor
     */
    fun setColors(
        @ColorInt containerColor: Int,
        @ColorInt contentColor: Int,
        @ColorInt linkContentColor: Int,
    ) {
        colorSpec = ColorSpec(
            containerColor = containerColor,
            contentColor = contentColor,
            linkContentColor = linkContentColor
        )
    }

    /**
     * Sets the desired container, content and link content colours to be used
     * from the given colour resources.
     * @see setContainerColorResource
     * @see setContentColorResource
     * @see setLinkContentColorResource
     */
    fun setColorResources(
        @ColorRes containerColorRes: Int,
        @ColorRes contentColorRes: Int,
        @ColorRes linkContentColorRes: Int
    ) {
        colorSpec = ColorSpec(
            containerColor = context.getColor(containerColorRes),
            contentColor = context.getColor(contentColorRes),
            linkContentColor = context.getColor(linkContentColorRes)
        )
    }

    private fun TypedArray.getColorOrNull(@StyleableRes index: Int): Int? {
        if (!hasValue(index)) return null
        return getColor(index, -1).takeIf { it >= 0 }
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.MarkdownView) {
            getString(R.styleable.MarkdownView_android_text)?.let { markdownText = it }
            isTextSelectable =
                getBoolean(R.styleable.MarkdownView_android_textIsSelectable, false)
            contentPadding = if (hasValue(R.styleable.MarkdownView_markdown_contentPadding)) {
                PaddingValues(
                    getDimension(
                        R.styleable.MarkdownView_markdown_contentPadding,
                        16f
                    ).dp
                )
            } else {
                PaddingValues(
                    top = getDimension(
                        R.styleable.MarkdownView_markdown_contentPaddingTop,
                        16f
                    ).dp,
                    bottom = getDimension(
                        R.styleable.MarkdownView_markdown_contentPaddingBottom,
                        16f
                    ).dp,
                    start = getDimension(
                        R.styleable.MarkdownView_markdown_contentPaddingStart,
                        16f
                    ).dp,
                    end = getDimension(
                        R.styleable.MarkdownView_markdown_contentPaddingEnd,
                        16f
                    ).dp
                )
            }
            colorSpec = ColorSpec(
                containerColor = getColorOrNull(
                    R.styleable.MarkdownView_markdown_containerColor
                ),
                contentColor = getColorOrNull(
                    R.styleable.MarkdownView_markdown_contentColor
                ),
                linkContentColor = getColorOrNull(
                    R.styleable.MarkdownView_markdown_linkContentColor
                )
            )
        }

        if (isInEditMode) {
            markdownText = SampleMarkdownText
        }
    }

    private val linkClickListeners = mutableListOf<(LinkAnnotation) -> Unit>()

    /** Adds a listener to be invoked when a [LinkAnnotation] is clicked. */
    fun addOnLinkClickListener(listener: (LinkAnnotation) -> Unit) {
        linkClickListeners += listener
    }

    /** Removes the given listener to be invoked when a [LinkAnnotation] is clicked. */
    fun removeOnLinkClickListener(listener: (LinkAnnotation) -> Unit) {
        linkClickListeners -= listener
    }

    /** Clears the list of listeners that are invoked when a [LinkAnnotation] is clicked. */
    fun clearOnLinkClickListeners() {
        linkClickListeners.clear()
    }

    @Parcelize
    internal data class SavedState(
        val superSavedState: Parcelable,
        val text: String,
        val isSelectable: Boolean
    ) : BaseSavedState(superSavedState), Parcelable

    override fun onSaveInstanceState(): Parcelable {
        return SavedState(
            super.onSaveInstanceState() ?: Bundle.EMPTY,
            text = markdownText,
            isSelectable = isTextSelectable
        )
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is SavedState) {
            super.onRestoreInstanceState(state.superSavedState)
            markdownText = state.text
            isTextSelectable = state.isSelectable
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    @Composable
    private fun ViewContent(modifier: Modifier = Modifier) {
        val linkContentColor by animateColorAsState(colorSpec.linkContent())

        MarkdownViewer(
            modifier = modifier.padding(contentPadding),
            markdownText = markdownText,
            linkContentColor = linkContentColor,
            onLinkClick = { annotation ->
                println("Link clicked: $annotation, listeners: $linkClickListeners")
                linkClickListeners.forEach { it(annotation) }
            }
        )
    }

    @Composable
    override fun Content() {
        StudyBuddyTheme {
            val content = remember { movableContentOf { ViewContent() } }
            val containerColor by animateColorAsState(colorSpec.container())
            val contentColor by animateColorAsState(colorSpec.content())

            Surface(
                color = containerColor,
                contentColor = contentColor
            ) {
                if (isTextSelectable) {
                    SelectionContainer(content = content)
                } else {
                    content()
                }
            }
        }
    }
}
