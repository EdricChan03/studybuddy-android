package com.edricchan.studybuddy.extensions.markwon

import android.content.Context
import android.widget.TextView.BufferType
import com.edricchan.studybuddy.extensions.markwon
import io.noties.markwon.Markwon.TextSetter
import io.noties.markwon.MarkwonPlugin

class MarkwonBuilderDSL(private val context: Context) {
    /** @see io.noties.markwon.Markwon.Builder.bufferType */
    var bufferType: BufferType? = null

    /** @see io.noties.markwon.Markwon.Builder.textSetter */
    var setter: TextSetter? = null

    fun setTextSetter(textSetter: TextSetter) {
        setter = textSetter
    }

    /** @see io.noties.markwon.Markwon.Builder.usePlugins */
    var plugins: MutableList<MarkwonPlugin> = mutableListOf()

    /** @see io.noties.markwon.Markwon.Builder.usePlugin */
    fun usePlugin(plugin: MarkwonPlugin) {
        plugins += plugin
    }

    /** @see io.noties.markwon.Markwon.Builder.usePlugins */
    fun usePlugins(vararg plugins: MarkwonPlugin) {
        this.plugins += plugins
    }

    /** @see io.noties.markwon.Markwon.Builder.usePlugins */
    fun usePlugins(plugins: List<MarkwonPlugin>) {
        this.plugins += plugins
    }

    /** @see io.noties.markwon.Markwon.Builder.fallbackToRawInputWhenEmpty */
    var fallbackToRawInputWhenEmpty: Boolean = true

    /** Whether to skip adding the [io.noties.markwon.core.CorePlugin] plugin. */
    var noCorePlugin: Boolean = false

    /** Builds an instance of [Markwon], given the arguments. */
    fun build() = context.markwon(noCorePlugin) {
        bufferType?.let { bufferType(it) }
        setter?.let { textSetter(it) }
        usePlugins(plugins)
        fallbackToRawInputWhenEmpty(fallbackToRawInputWhenEmpty)
    }
}

/**
 * Creates an instance of [io.noties.markwon.Markwon] using DSL syntax.
 */
fun Context.markwon(block: MarkwonBuilderDSL.() -> Unit) =
    MarkwonBuilderDSL(this).apply(block).build()
