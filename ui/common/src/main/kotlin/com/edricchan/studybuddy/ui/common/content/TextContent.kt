package com.edricchan.studybuddy.ui.common.content

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml

/**
 * Generic sealed hierarchy to hold textual data. Its main implementations are:
 * * [Str], which stores the [String] itself
 * * [Res], which stores a string resource
 * To retrieve the textual data, use the [asString] method.
 */
sealed interface TextContent {
    @JvmInline
    value class Str(val value: String) : TextContent {
        override fun asString(context: Context): String = value

        override fun asCharSequence(context: Context): CharSequence = value

        @Composable
        override fun asAnnotatedString(): AnnotatedString = AnnotatedString(value)
    }

    @JvmInline
    value class Res(@field:StringRes val stringRes: Int) : TextContent {
        override fun asString(context: Context): String = context.getString(stringRes)

        override fun asCharSequence(context: Context): CharSequence = context.getText(stringRes)

        @Composable
        override fun asAnnotatedString(): AnnotatedString =
            AnnotatedString.fromHtml(stringResource(stringRes))
    }

    /** Gets the non-styled variant of this content. */
    fun asString(context: Context): String

    @Composable
    fun asString(): String = asString(LocalContext.current)

    /** Gets the styled variant of this content. */
    fun asCharSequence(context: Context): CharSequence

    /** Gets the content as an [AnnotatedString]. */
    @Composable
    fun asAnnotatedString(): AnnotatedString
}
