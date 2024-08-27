package com.edricchan.studybuddy.utils.recyclerview.holder

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding

/**
 * [ViewHolder] that supports [ViewBinding].
 *
 * [onBind] should be implemented to bind an item to this view-holder.
 * @property binding The [VB] instance.
 * @param T Item type to be used.
 * @param VB [ViewBinding] type to be used.
 */
abstract class ViewBindingHolder<T, VB : ViewBinding>(
    protected val binding: VB
) : ViewHolder(binding.root) {
    /** Binds the specified [item] to this `ViewHolder`. */
    fun bind(item: T) = binding.onBind(item)

    /** Binds the specified [item] to this `ViewHolder`. Called by [bind]. */
    protected abstract fun VB.onBind(item: T)
}
