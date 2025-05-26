@file:OptIn(ExperimentalAtomicApi::class)

package com.edricchan.studybuddy.utils.markwon

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Spanned
import android.widget.TextView
import coil3.Image
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.asDrawable
import coil3.request.Disposable
import coil3.request.ImageRequest
import coil3.target.Target
import com.edricchan.studybuddy.utils.markwon.CoilImagesPlugin.CoilStore
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.MarkwonConfiguration
import io.noties.markwon.MarkwonSpansFactory
import io.noties.markwon.image.AsyncDrawable
import io.noties.markwon.image.AsyncDrawableLoader
import io.noties.markwon.image.AsyncDrawableScheduler
import io.noties.markwon.image.DrawableUtils
import io.noties.markwon.image.ImageSpanFactory
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import org.commonmark.node.Image as ImageNode

// Modified from
// https://github.com/noties/Markwon/blob/2ea148c30a07f91ffa37c0aa36af1cf2670441af/markwon-image-coil/src/main/java/io/noties/markwon/image/coil/CoilImagesPlugin.java
// with Coil3 support and migrated to Kotlin
class CoilImagesPlugin internal constructor(
    context: Context,
    coilStore: CoilStore,
    imageLoader: ImageLoader
) : AbstractMarkwonPlugin() {
    private val coilAsyncDrawableLoader: CoilAsyncDrawableLoader =
        CoilAsyncDrawableLoader(context, coilStore, imageLoader)

    override fun configureSpansFactory(builder: MarkwonSpansFactory.Builder) {
        builder.setFactory<ImageNode?>(ImageNode::class.java, ImageSpanFactory())
    }

    override fun configureConfiguration(builder: MarkwonConfiguration.Builder) {
        builder.asyncDrawableLoader(coilAsyncDrawableLoader)
    }

    override fun beforeSetText(textView: TextView, markdown: Spanned) {
        AsyncDrawableScheduler.unschedule(textView)
    }

    override fun afterSetText(textView: TextView) {
        AsyncDrawableScheduler.schedule(textView)
    }

    interface CoilStore {
        fun load(drawable: AsyncDrawable): ImageRequest

        fun cancel(disposable: Disposable)
    }

    private class CoilAsyncDrawableLoader(
        private val context: Context,
        private val coilStore: CoilStore,
        private val imageLoader: ImageLoader
    ) : AsyncDrawableLoader() {
        private val cache: MutableMap<AsyncDrawable?, Disposable?> =
            mutableMapOf<AsyncDrawable?, Disposable?>()

        override fun load(drawable: AsyncDrawable) {
            val loaded = AtomicBoolean(false)
            val target = AsyncDrawableTarget(drawable, loaded)
            val request = coilStore.load(drawable).newBuilder()
                .target(target)
                .build()
            // @since 4.5.1 execute can return result _before_ disposable is created,
            //  thus `execute` would finish before we put disposable in cache (and thus result is
            //  not delivered)
            val disposable = imageLoader.enqueue(request)
            // if flag was not set, then job is running (else - finished before we got here)
            if (loaded.compareAndSet(expectedValue = false, newValue = true)) {
                // mark flag
                cache.put(drawable, disposable)
            }
        }

        override fun cancel(drawable: AsyncDrawable) {
            val disposable = cache.remove(drawable)
            disposable?.let { coilStore.cancel(it) }
        }

        override fun placeholder(drawable: AsyncDrawable): Drawable? {
            return null
        }

        private inner class AsyncDrawableTarget(
            private val drawable: AsyncDrawable,
            private val loaded: AtomicBoolean
        ) : Target {
            override fun onSuccess(result: Image) {
                // @since 4.5.1 check finished flag (result can be delivered _before_ disposable is created)
                if (cache.remove(drawable) != null
                    || loaded.compareAndSet(expectedValue = false, newValue = true)
                ) {
                    // mark
                    if (drawable.isAttached) {
                        val loadedDrawable = result.asDrawable(context.resources)
                        DrawableUtils.applyIntrinsicBoundsIfEmpty(loadedDrawable)
                        drawable.result = loadedDrawable
                    }
                }
            }

            override fun onStart(placeholder: Image?) {
                if (placeholder != null && drawable.isAttached) {
                    val placeholderDrawable = placeholder.asDrawable(context.resources)
                    DrawableUtils.applyIntrinsicBoundsIfEmpty(placeholderDrawable)
                    drawable.result = placeholderDrawable
                }
            }

            override fun onError(error: Image?) {
                if (cache.remove(drawable) != null) {
                    if (error != null && drawable.isAttached) {
                        val errorDrawable = error.asDrawable(context.resources)
                        DrawableUtils.applyIntrinsicBoundsIfEmpty(errorDrawable)
                        drawable.result = errorDrawable
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun create(
            context: Context,
            imageLoader: ImageLoader
        ): CoilImagesPlugin = create(
            context = context,
            coilStore = context.coilStore,
            imageLoader = imageLoader
        )

        @JvmStatic
        @JvmOverloads
        fun create(
            context: Context,
            coilStore: CoilStore = context.coilStore,
            imageLoader: ImageLoader = SingletonImageLoader.get(context)
        ): CoilImagesPlugin = CoilImagesPlugin(
            context = context,
            coilStore = coilStore,
            imageLoader = imageLoader
        )
    }
}

val Context.coilStore: CoilStore
    get() = object : CoilStore {
        override fun load(drawable: AsyncDrawable): ImageRequest {
            return ImageRequest.Builder(this@coilStore)
                .data(drawable.destination)
                .build()
        }

        override fun cancel(disposable: Disposable) {
            disposable.dispose()
        }
    }
