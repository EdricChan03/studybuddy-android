package com.edricchan.studybuddy.ui.common.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.edricchan.studybuddy.exts.androidx.viewbinding.viewInflateBinding

/**
 * [BaseFragment] that is backed by a [VB], which can be accessed via
 * the [binding] property.
 * @param bindingInflater Method used to create the [VB].
 * @param VB [ViewBinding] type to use for this class.
 */
abstract class ViewBindingFragment<VB : ViewBinding>(
    bindingInflater: (LayoutInflater) -> VB
) : BaseFragment() {
    protected val binding: VB by viewInflateBinding(bindingInflater)

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root
}
