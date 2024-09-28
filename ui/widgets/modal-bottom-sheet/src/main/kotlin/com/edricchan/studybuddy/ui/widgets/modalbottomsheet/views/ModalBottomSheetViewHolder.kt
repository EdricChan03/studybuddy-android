package com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views

import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.interfaces.ModalBottomSheetItem
import com.edricchan.studybuddy.utils.recyclerview.holder.ViewBindingHolder

sealed class ModalBottomSheetViewHolder<VB : ViewBinding>(
    binding: VB,
    val requestDismiss: () -> Unit
) : ViewBindingHolder<ModalBottomSheetItem, VB>(binding) {
    protected abstract fun VB.onSubBind(item: ModalBottomSheetItem)

    override fun VB.onBind(item: ModalBottomSheetItem) {
        with(itemView) {
            isEnabled = item.enabled
            if (!item.visible) {
                isGone = true
                layoutParams = RecyclerView.LayoutParams(0, 0)
            }
            setOnClickListener {
                item.onItemClickListener?.onItemClick(item)
                if (item.requestDismissOnClick) requestDismiss()
            }
        }

        onSubBind(item)
    }
}
