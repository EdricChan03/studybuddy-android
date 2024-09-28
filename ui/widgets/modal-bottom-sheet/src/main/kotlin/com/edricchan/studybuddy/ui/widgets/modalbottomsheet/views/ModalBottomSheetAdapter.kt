package com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.databinding.BottomSheetItemRowNoIconBinding
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.databinding.BottomSheetItemRowWithIconBinding
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.interfaces.ModalBottomSheetGroup
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.interfaces.ModalBottomSheetGroup.CheckableBehavior
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.interfaces.ModalBottomSheetItem
import com.edricchan.studybuddy.utils.recyclerview.diffCallback

class ModalBottomSheetAdapter(
    val requestDismiss: () -> Unit
) : ListAdapter<ModalBottomSheetItem, ModalBottomSheetViewHolder<*>>(DIFF_CALLBACK) {
    @Deprecated("Submit lists via submitList()")
    constructor(
        context: Context,
        items: List<ModalBottomSheetItem>,
        requestDismiss: () -> Unit
    ) : this(requestDismiss) {
        submitList(items)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ModalBottomSheetViewHolder<*> {
        return if (viewType and FlagIcon != 0) {
            WithIconViewHolder(
                binding = BottomSheetItemRowWithIconBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                requestDismiss = requestDismiss
            )
        } else {
            NoIconViewHolder(
                binding = BottomSheetItemRowNoIconBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                requestDismiss = requestDismiss
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.icon != null) {
            if (item.group != null && item.group.id != ModalBottomSheetGroup.ID_NONE) {
                FlagIcon or FlagCheckable
            } else {
                FlagIcon
            }
        } else {
            if (item.group != null && item.group.id != ModalBottomSheetGroup.ID_NONE) {
                FlagCheckable
            } else {
                0
            }
        }
    }

    override fun onBindViewHolder(holder: ModalBottomSheetViewHolder<*>, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class NoIconViewHolder(
        binding: BottomSheetItemRowNoIconBinding,
        requestDismiss: () -> Unit
    ) : ModalBottomSheetViewHolder<BottomSheetItemRowNoIconBinding>(
        binding = binding,
        requestDismiss = requestDismiss
    ) {
        override fun BottomSheetItemRowNoIconBinding.onSubBind(item: ModalBottomSheetItem) {
            itemTitle.text = item.title

            if (itemViewType and FlagCheckable != 0) {
                when (item.group?.checkableBehaviorEnum) {
                    CheckableBehavior.All -> {
                        with(itemCheckBox) {
                            isVisible = true
                            isChecked = item in item.group.selected
                        }
                    }

                    CheckableBehavior.Single -> {
                        with(itemRadioButton) {
                            isVisible = true
                            isChecked = item in item.group.selected
                        }
                    }

                    else -> {} // No-op
                }
            }
        }
    }

    class WithIconViewHolder(
        binding: BottomSheetItemRowWithIconBinding,
        requestDismiss: () -> Unit
    ) : ModalBottomSheetViewHolder<BottomSheetItemRowWithIconBinding>(
        binding = binding,
        requestDismiss = requestDismiss
    ) {
        override fun BottomSheetItemRowWithIconBinding.onSubBind(item: ModalBottomSheetItem) {
            itemTitle.text = item.title

            item.icon?.let { icon ->
                itemIcon.setImageBitmap(icon.asBitmap(itemView.context))
            }

            if (itemViewType and FlagCheckable != 0) {
                when (item.group?.checkableBehaviorEnum) {
                    CheckableBehavior.All -> {
                        with(itemCheckBox) {
                            isVisible = true
                            isChecked = item in item.group.selected
                        }
                    }

                    CheckableBehavior.Single -> {
                        with(itemRadioButton) {
                            isVisible = true
                            isChecked = item in item.group.selected
                        }
                    }

                    else -> {} // No-op
                }
            }
        }
    }

    /** Listener for when an item's checked state is toggled */
    fun interface OnItemCheckedChangeListener {
        /**
         * Called when an item's checked state is toggled
         * @param item The item whose checked state was toggled
         */
        fun onItemCheckedChange(item: ModalBottomSheetItem)
    }

    /** Listener for when an item has been clicked on */
    fun interface OnItemClickListener {
        /**
         * Called when an item has been clicked on
         * @param item The item that was clicked on
         */
        fun onItemClick(item: ModalBottomSheetItem)
    }

    companion object {
        private const val FlagIcon = 1 shl 0
        private const val FlagCheckable = 1 shl 1

        val DIFF_CALLBACK: DiffUtil.ItemCallback<ModalBottomSheetItem> = diffCallback(
            areItemsTheSame = { old, new -> old.id == new.id }
        )
    }
}
