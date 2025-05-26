package com.edricchan.studybuddy.exts.markwon

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import coil3.ImageLoader
import com.edricchan.studybuddy.utils.markwon.CoilImagesPlugin
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tasklist.TaskListPlugin
import io.noties.markwon.linkify.LinkifyPlugin

val Context.coilImagesPlugin get() = CoilImagesPlugin.create(this)

fun Context.coilImagesPlugin(imageLoader: ImageLoader) =
    CoilImagesPlugin.create(this, imageLoader)

val linkifyPlugin = LinkifyPlugin.create()

fun linkifyPlugin(useCompat: Boolean) = LinkifyPlugin.create(useCompat)

val Context.taskListPlugin get() = TaskListPlugin.create(this)

fun Context.taskListPlugin(drawable: Drawable) = TaskListPlugin.create(drawable)

fun Context.taskListPlugin(
    @ColorInt checkedFillColor: Int,
    @ColorInt normalOutlineColor: Int,
    @ColorInt checkMarkColor: Int
) = TaskListPlugin.create(checkedFillColor, normalOutlineColor, checkMarkColor)

val strikethroughPlugin = StrikethroughPlugin.create()
