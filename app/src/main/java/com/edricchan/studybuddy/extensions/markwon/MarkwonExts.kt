package com.edricchan.studybuddy.extensions

import android.content.Context
import android.text.Spanned
import android.widget.TextView
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonPlugin

/**
 * Retrieves a minimal [Markwon] instance from the context.
 *
 * To customise [Markwon], use the function that takes in a [Markwon.Builder].
 */
val Context.markwon get() = Markwon.create(this)

/**
 * Retrieves an instance of [Markwon] from the [Context].
 * @param noCore Whether to exclude the [io.noties.markwon.core.CorePlugin] plugin.
 * @param init A lambda that takes in a [Markwon.Builder] and configures it.
 */
fun Context.markwon(noCore: Boolean, init: Markwon.Builder.() -> Unit) =
    (if (noCore) Markwon.builderNoCore(this) else Markwon.builder(this))
        .apply(init).build()

/**
 * Sets the text of the [TextView] to the markdown-formatted text.
 * @param markwon The instance of [Markwon] to use.
 * @param markdown The markdown-formatted text.
 */
fun TextView.setMarkdown(markwon: Markwon, markdown: String) {
    markwon.setMarkdown(this, markdown)
}

/**
 * Sets the text of the [TextView] to the markdown-formatted text.
 * @param markwon The instance of [Markwon] to use.
 * @param markdown The markdown-formatted text.
 */
fun TextView.setParsedMarkdown(markwon: Markwon, markdown: Spanned) {
    markwon.setParsedMarkdown(this, markdown)
}

fun String.toMarkdown(markwon: Markwon) = markwon.toMarkdown(this)

/**
 * Sets the list of [MarkwonPlugin]s to use.
 * @param plugins The list of plugins to use, as a `vararg` array.
 */
fun Markwon.Builder.usePlugins(vararg plugins: MarkwonPlugin) =
    usePlugins(plugins.toList())

/**
 * Checks whether the [Markwon] instance has the given [plugin specified as `T`][T].
 * This overload allows for a `reified` version of the [MarkwonPlugin] type to be passed.
 * @param T The type of the [MarkwonPlugin] to check for.
 * @see Markwon.hasPlugin
 */
inline fun <reified T : MarkwonPlugin> Markwon.hasPlugin() = hasPlugin(T::class.java)

/**
 * Retrieves the given [plugin specified as `T`][T], or `null` if no such plugin was set
 * on this instance.
 * This overload allows for a `reified` version of the [MarkwonPlugin] type to be passed.
 * @param T The type of the [MarkwonPlugin] to retrieve.
 * @see Markwon.getPlugin
 */
inline fun <reified T : MarkwonPlugin> Markwon.getPlugin() = getPlugin(T::class.java)

/**
 * Retrieves the given [plugin specified as `T`][T], or throws an
 * [IllegalStateException] if no such plugin was set on this instance.
 * This overload allows for a `reified` version of the [MarkwonPlugin] type to be passed.
 * @param T The type of the [MarkwonPlugin] to retrieve.
 * @see Markwon.requirePlugin
 */
inline fun <reified T : MarkwonPlugin> Markwon.requirePlugin() = requirePlugin(T::class.java)
