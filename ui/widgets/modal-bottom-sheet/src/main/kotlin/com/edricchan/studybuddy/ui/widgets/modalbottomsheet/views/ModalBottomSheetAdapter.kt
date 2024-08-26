package com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.R
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.interfaces.ModalBottomSheetGroup
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.interfaces.ModalBottomSheetItem

class ModalBottomSheetAdapter(
    private val context: Context,
    private val items: List<ModalBottomSheetItem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LIST_ITEM_NO_ICON, LIST_ITEM_NO_ICON_CHECKBOX, LIST_ITEM_NO_ICON_RADIO_BUTTON -> {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.modalbottomsheetadapter_item_row_no_icon, parent, false)
                when (viewType) {
                    LIST_ITEM_NO_ICON -> NoIconWithTextHolder(itemView)
                    LIST_ITEM_NO_ICON_CHECKBOX -> NoIconWithTextWithCheckboxHolder(itemView)
                    LIST_ITEM_NO_ICON_RADIO_BUTTON -> NoIconWithTextWithRadioButtonHolder(itemView)
                    else -> NoIconWithTextHolder(itemView)
                }
            }

            LIST_ITEM_ICON, LIST_ITEM_ICON_CHECKBOX, LIST_ITEM_ICON_RADIO_BUTTON -> {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.modalbottomsheetadapter_item_row_with_icon, parent, false)
                when (viewType) {
                    LIST_ITEM_ICON -> IconWithTextHolder(itemView)
                    LIST_ITEM_ICON_CHECKBOX -> IconWithTextWithCheckboxHolder(itemView)
                    LIST_ITEM_ICON_RADIO_BUTTON -> IconWithTextWithRadioButtonHolder(itemView)
                    else -> IconWithTextHolder(itemView)
                }
            }

            else -> {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.modalbottomsheetadapter_item_row_no_icon, parent, false)
                NoIconWithTextHolder(itemView)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        return if (item.icon != null) {
            if (item.group != null && item.group?.id != ModalBottomSheetGroup.ID_NONE) {
                when (item.group?.checkableBehavior) {
                    ModalBottomSheetGroup.CHECKABLE_BEHAVIOR_ALL -> LIST_ITEM_ICON_CHECKBOX
                    ModalBottomSheetGroup.CHECKABLE_BEHAVIOR_SINGLE -> LIST_ITEM_ICON_RADIO_BUTTON
                    else -> LIST_ITEM_ICON
                }
            } else {
                LIST_ITEM_ICON
            }
        } else {
            if (item.group != null && item.group?.id != ModalBottomSheetGroup.ID_NONE) {
                when (item.group?.checkableBehavior) {
                    ModalBottomSheetGroup.CHECKABLE_BEHAVIOR_ALL -> LIST_ITEM_NO_ICON_CHECKBOX
                    ModalBottomSheetGroup.CHECKABLE_BEHAVIOR_SINGLE -> LIST_ITEM_NO_ICON_RADIO_BUTTON
                    else -> LIST_ITEM_NO_ICON
                }
            } else {
                LIST_ITEM_NO_ICON
            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        val itemGroup = item.group
        holder.itemView.isEnabled = item.enabled
        if (!item.visible) {
            holder.itemView.isGone = true
            holder.itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
        }
        when (holder.itemViewType) {
            LIST_ITEM_NO_ICON -> {
                val tempHolder = holder as NoIconWithTextHolder
                tempHolder.titleTextView.text = item.title

                // Handle on click listener in item
                if (item.onItemClickListener != null) {
                    holder.itemView.setOnClickListener {
                        item.onItemClickListener?.onItemClick(item)
                    }
                }
            }

            LIST_ITEM_NO_ICON_CHECKBOX -> {
                val tempHolder = holder as NoIconWithTextWithCheckboxHolder
                tempHolder.titleTextView.text = item.title
                tempHolder.checkBox.isChecked = itemGroup?.selected?.contains(item) ?: false
                // Disable clicks on the checkbox
                tempHolder.checkBox.isClickable = false

                holder.itemView.setOnClickListener {
                    if (itemGroup?.selected?.contains(item) == true) {
                        itemGroup.selected.remove(item)
                    } else {
                        itemGroup?.selected?.add(item)
                    }
                    tempHolder.checkBox.isChecked = itemGroup?.selected?.contains(item) ?: false

                    // Handle on item checked listener in item's group
                    if (itemGroup?.onItemCheckedChangeListener != null) {
                        itemGroup?.onItemCheckedChangeListener?.onItemCheckedChange(item)
                    }

                    // Handle on click listener in item
                    if (item.onItemClickListener != null) {
                        item.onItemClickListener?.onItemClick(item)
                    }
                }

            }

            LIST_ITEM_NO_ICON_RADIO_BUTTON -> {
                val tempHolder = holder as NoIconWithTextWithRadioButtonHolder
                tempHolder.titleTextView.text = item.title
                tempHolder.radioButton.isChecked = itemGroup?.selected?.contains(item) ?: false
                // Disable clicks on the radio button
                tempHolder.radioButton.isClickable = false

                holder.itemView.setOnClickListener {
                    if (itemGroup?.selected?.contains(item) == true) {
                        itemGroup.selected.remove(item)
                    } else {
                        // Clear any existing selected items
                        if (itemGroup?.selected?.size!! > 0) {
                            // Update state of items that are not the item that was clicked
                            itemGroup.selected.forEach { item ->
                                if (items.indexOf(item) != -1) {
                                    notifyItemChanged(items.indexOf(item))
                                }
                            }
                            itemGroup.selected.clear()
                        }
                        itemGroup.selected.add(item)
                    }
                    tempHolder.radioButton.isChecked = itemGroup.selected.contains(item)

                    // Handle on item checked listener in item's group
                    if (itemGroup.onItemCheckedChangeListener != null) {
                        itemGroup.onItemCheckedChangeListener?.onItemCheckedChange(item)
                    }

                    // Handle on click listener in item
                    if (item.onItemClickListener != null) {
                        item.onItemClickListener?.onItemClick(item)
                    }
                }
            }

            LIST_ITEM_ICON -> {
                val tempHolder = holder as IconWithTextHolder
                item.icon?.let { icon ->
                    tempHolder.iconImageView.setImageDrawable(icon.asDrawable(context))
                }
                tempHolder.titleTextView.text = item.title

                // Handle on click listener in item
                if (item.onItemClickListener != null) {
                    holder.itemView.setOnClickListener {
                        item.onItemClickListener?.onItemClick(item)
                    }
                }
            }

            LIST_ITEM_ICON_CHECKBOX -> {
                val tempHolder = holder as IconWithTextWithCheckboxHolder
                item.icon?.let {
                    tempHolder.iconImageView.setImageDrawable(it.asDrawable(context))
                }
                tempHolder.titleTextView.text = item.title
                tempHolder.checkBox.isChecked = itemGroup?.selected?.contains(item) ?: false
                // Disable clicks on the checkbox
                tempHolder.checkBox.isClickable = false

                holder.itemView.setOnClickListener {
                    if (itemGroup?.selected?.contains(item) == true) {
                        itemGroup.selected.remove(item)
                    } else {
                        itemGroup?.selected?.add(item)
                    }
                    tempHolder.checkBox.isChecked = itemGroup?.selected?.contains(item) ?: false

                    // Handle on item checked listener in item's group
                    if (itemGroup?.onItemCheckedChangeListener != null) {
                        itemGroup?.onItemCheckedChangeListener?.onItemCheckedChange(item)
                    }

                    // Handle on click listener in item
                    if (item.onItemClickListener != null) {
                        item.onItemClickListener?.onItemClick(item)
                    }
                }
            }

            LIST_ITEM_ICON_RADIO_BUTTON -> {
                val tempHolder = holder as IconWithTextWithRadioButtonHolder

                val tempCheckedState = itemGroup?.selected?.contains(item) ?: false
                tempHolder.radioButton.isChecked = tempCheckedState
                // Disable clicks on the radio button
                tempHolder.radioButton.isClickable = false
                item.icon?.let { icon ->
                    tempHolder.iconImageView.setImageDrawable(icon.asDrawable(context))
                }

                tempHolder.titleTextView.text = item.title

                holder.itemView.setOnClickListener {
                    if (itemGroup?.selected?.contains(item) == true) {
                        itemGroup.selected.remove(item)
                    } else {
                        // Clear any existing selected items
                        if (itemGroup?.selected?.size!! > 0) {
                            // Update state of items that are not the item that was clicked
                            itemGroup.selected.forEach { item ->
                                if (items.indexOf(item) != -1) {
                                    notifyItemChanged(items.indexOf(item))
                                }
                            }
                            itemGroup.selected.clear()
                        }
                        itemGroup.selected.add(item)
                    }
                    tempHolder.radioButton.isChecked = itemGroup.selected.contains(item)

                    // Handle on item checked listener in item's group
                    if (itemGroup.onItemCheckedChangeListener != null) {
                        itemGroup.onItemCheckedChangeListener?.onItemCheckedChange(item)
                    }

                    // Handle on click listener in item
                    if (item.onItemClickListener != null) {
                        item.onItemClickListener.onItemClick(item)
                    }

                }
            }
        }
    }

    open inner class TextHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var titleTextView: TextView = itemView.findViewById(R.id.itemTitle)
    }

    open inner class IconWithTextHolder(itemView: View) : TextHolder(itemView) {
        internal var iconImageView: ImageView = itemView.findViewById(R.id.itemIcon)
    }

    open inner class NoIconWithTextHolder(itemView: View) : TextHolder(itemView)

    inner class IconWithTextWithCheckboxHolder(itemView: View) : IconWithTextHolder(itemView) {
        internal var checkBox: CheckBox = itemView.findViewById(R.id.itemCheckBox)

        init {
            checkBox.isVisible = true
        }
    }

    inner class NoIconWithTextWithCheckboxHolder(itemView: View) : NoIconWithTextHolder(itemView) {
        internal var checkBox: CheckBox = itemView.findViewById(R.id.itemCheckBox)

        init {
            checkBox.isVisible = true
        }
    }

    inner class IconWithTextWithRadioButtonHolder(itemView: View) : IconWithTextHolder(itemView) {
        internal var radioButton: RadioButton = itemView.findViewById(R.id.itemRadioButton)

        init {
            radioButton.isVisible = true
        }
    }

    inner class NoIconWithTextWithRadioButtonHolder(itemView: View) :
        NoIconWithTextHolder(itemView) {
        internal var radioButton: RadioButton = itemView.findViewById(R.id.itemRadioButton)

        init {
            radioButton.isVisible = true
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
        private const val LIST_ITEM_NO_ICON = 0
        private const val LIST_ITEM_NO_ICON_CHECKBOX = 1
        private const val LIST_ITEM_NO_ICON_RADIO_BUTTON = 2
        private const val LIST_ITEM_ICON = 3
        private const val LIST_ITEM_ICON_CHECKBOX = 4
        private const val LIST_ITEM_ICON_RADIO_BUTTON = 5
    }
}
