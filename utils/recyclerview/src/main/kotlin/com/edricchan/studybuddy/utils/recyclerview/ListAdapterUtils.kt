package com.edricchan.studybuddy.utils.recyclerview

import androidx.recyclerview.widget.DiffUtil

/**
 * Convenience method to create a [DiffUtil.ItemCallback].
 * @param areItemsTheSame The function to be invoked by [DiffUtil.ItemCallback.areItemsTheSame].
 * @param areContentsTheSame The function to be invoked by [DiffUtil.ItemCallback.areContentsTheSame].
 */
fun <T : Any> diffCallback(
    areItemsTheSame: (T, T) -> Boolean,
    areContentsTheSame: (T, T) -> Boolean
) =
    object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(old: T, new: T) = areItemsTheSame(old, new)
        override fun areContentsTheSame(old: T, new: T) = areContentsTheSame(old, new)
    }
