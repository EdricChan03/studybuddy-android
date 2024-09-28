package com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.edricchan.studybuddy.exts.androidx.viewbinding.viewInflateBinding
import com.edricchan.studybuddy.ui.insets.enableEdgeToEdge
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.databinding.FragModalBottomSheetBinding
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.ModalBottomSheetFragment.Companion.newInstance
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.interfaces.ModalBottomSheetItem
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.vm.ModalBottomSheetViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

/**
 * A bottom sheet which can be used to show more options for a particular action..
 *
 * To create an instance of this class, use [newInstance].
 */
class ModalBottomSheetFragment
// Not to be instantiated directly; `newInstance` should be used
private constructor() : BottomSheetDialogFragment() {
    private val viewModel by viewModels<ModalBottomSheetViewModel>()

    private val binding: FragModalBottomSheetBinding by viewInflateBinding(
        FragModalBottomSheetBinding::inflate
    )

    private lateinit var adapter: ModalBottomSheetAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            bottomSheetRecyclerView.apply {
                adapter = ModalBottomSheetAdapter(requestDismiss = ::dismiss).apply {
                    this@ModalBottomSheetFragment.adapter = this
                }
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch {
                        viewModel.headerTitleState.collect {
                            bottomSheetHeader.isVisible = it != null
                            bottomSheetHeaderTitleTextView.text = it
                        }
                    }

                    launch {
                        viewModel.hideDragHandleState.collect {
                            bottomSheetDragHandle.isGone = it
                        }
                    }

                    launch {
                        viewModel.itemsState.collect(adapter::submitList)
                    }
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        super.onCreateDialog(savedInstanceState).also { dialog ->
            dialog.window?.enableEdgeToEdge()
        }

    companion object {
        internal const val TAG_HEADER_TITLE = "bottom-sheet:headerTitle"
        internal const val TAG_ITEMS = "bottom-sheet:items"
        internal const val TAG_HIDE_DRAG_HANDLE = "bottom-sheet:hideDragHandle"

        /**
         * Creates a new instance of [ModalBottomSheetFragment] with the specified
         * arguments.
         */
        fun newInstance(
            items: List<ModalBottomSheetItem>,
            headerTitle: String? = null,
            hideDragHandle: Boolean = false
        ) = ModalBottomSheetFragment().apply {
            arguments = bundleOf(
                TAG_HEADER_TITLE to headerTitle,
                TAG_ITEMS to items,
                TAG_HIDE_DRAG_HANDLE to hideDragHandle
            )
        }
    }
}
