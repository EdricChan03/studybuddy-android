package com.edricchan.studybuddy.utils.recyclerview

import androidx.recyclerview.widget.DiffUtil

/**
 * Convenience method to create a [DiffUtil.ItemCallback].
 * @param areItemsTheSame The function to be invoked by [DiffUtil.ItemCallback.areItemsTheSame].
 * @param areContentsTheSame The function to be invoked by [DiffUtil.ItemCallback.areContentsTheSame].
 */
inline fun <T : Any> diffCallback(
    crossinline areItemsTheSame: (oldItem: T, newItem: T) -> Boolean,
    crossinline areContentsTheSame: (oldItem: T, newItem: T) -> Boolean = { old, new -> old == new }
) = object : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(old: T, new: T) = areItemsTheSame(old, new)
    override fun areContentsTheSame(old: T, new: T) = areContentsTheSame(old, new)
}
