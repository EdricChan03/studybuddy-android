package com.edricchan.studybuddy.exts.androidx.viewbinding

import android.app.Activity
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

/**
 * Sets the receiver [Activity]'s content to the [binding]'s [ViewBinding.getRoot].
 * @see Activity.setContentView
 */
fun <VB : ViewBinding> Activity.setContentView(binding: VB) {
    setContentView(binding.root)
}

/**
 * Sets the receiver [Activity]'s content to the [binding]'s [ViewBinding.getRoot]
 * with the specified [params] to be set for the `view`.
 * @see Activity.setContentView
 */
fun <VB : ViewBinding, LP : ViewGroup.LayoutParams> Activity.setContentView(
    binding: VB,
    params: LP
) {
    setContentView(binding.root, params)
}
