package com.edricchan.studybuddy.ui.widget.bottomsheet

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.ui.widget.bottomsheet.interfaces.ModalBottomSheetGroup
import com.edricchan.studybuddy.ui.widget.bottomsheet.interfaces.ModalBottomSheetItem
import com.edricchan.studybuddy.utils.SharedUtils

class ModalBottomSheetAdapter(
		private val context: Context,
		private val items: Array<ModalBottomSheetItem>,
		private val groups: Array<ModalBottomSheetGroup>
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
		return if (items[position].icon != null) {
			if (items[position].groupId != ModalBottomSheetItem.GROUP_ID_NONE) {
				when (groups.find { it.id == items[position].groupId }?.checkableBehavior) {
					ModalBottomSheetGroup.CHECKABLE_BEHAVIOR_ALL -> LIST_ITEM_ICON_CHECKBOX
					ModalBottomSheetGroup.CHECKABLE_BEHAVIOR_SINGLE -> LIST_ITEM_ICON_RADIO_BUTTON
					else -> LIST_ITEM_ICON
				}
			} else {
				LIST_ITEM_ICON
			}
		} else {
			if (items[position].groupId != ModalBottomSheetItem.GROUP_ID_NONE) {
				when (groups.find { it.id == items[position].groupId }?.checkableBehavior) {
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
		val itemGroup = groups.find { it.id == item.groupId }
		Log.d(TAG, "Item: $item")
		Log.d(TAG, "Item group: ${itemGroup.toString()}")
		holder.itemView.isEnabled = item.enabled
		if (!item.visible) {
			holder.itemView.visibility = View.GONE
			holder.itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
		}
		when (holder.itemViewType) {
			LIST_ITEM_NO_ICON -> {
				val tempHolder = holder as NoIconWithTextHolder
				tempHolder.titleTextView.text = item.title

				if (item.onClickListener != null) {
					holder.itemView.setOnClickListener(item.onClickListener)
				}
			}
			LIST_ITEM_NO_ICON_CHECKBOX -> {
				val tempHolder = holder as NoIconWithTextWithCheckboxHolder
				tempHolder.titleTextView.text = item.title
				tempHolder.checkBox.isChecked = itemGroup?.selected?.contains(item) ?: false
				holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
					if (isChecked && itemGroup?.selected?.contains(item) == false) {
						itemGroup.selected.add(item)
					} else {
						itemGroup?.selected?.remove(item)
					}
					// Handle on item checked listener in item's group
					if (itemGroup?.onItemCheckedChangeListener != null) {
						itemGroup.onItemCheckedChangeListener?.onItemCheckedChange(item)
					}
				}
				holder.itemView.setOnClickListener {
					if (itemGroup?.selected?.contains(item) == true) {
						itemGroup.selected.remove(item)
					} else {
						itemGroup?.selected?.add(item)
					}
					tempHolder.checkBox.isChecked = itemGroup?.selected?.contains(item) ?: false

					// Handle on item checked listener in item's group
					if (itemGroup?.onItemCheckedChangeListener != null) {
						itemGroup.onItemCheckedChangeListener?.onItemCheckedChange(item)
					}

					// Handle on click listener in item
					if (item.onClickListener != null) {
						item.onClickListener?.onClick(it)
					}
				}

			}
			LIST_ITEM_NO_ICON_RADIO_BUTTON -> {
				val tempHolder = holder as NoIconWithTextWithRadioButtonHolder
				tempHolder.titleTextView.text = item.title
				tempHolder.radioButton.isChecked = itemGroup?.selected?.contains(item) ?: false
				holder.radioButton.setOnCheckedChangeListener { _, isChecked ->
					if (isChecked && itemGroup?.selected?.contains(item) == false) {
						// Clear any existing selected items
						if (itemGroup.selected.size > 0) {
							// Update state of items that are not the item that was clicked
							itemGroup.selected.forEach { item ->
								if (items.indexOf(item) != -1) {
									notifyItemChanged(items.indexOf(item))
								}
							}
							itemGroup.selected.clear()
						}
						itemGroup.selected.add(item)
					} else {
						itemGroup?.selected?.remove(item)
					}
					// Handle on item checked listener in item's group
					if (itemGroup?.onItemCheckedChangeListener != null) {
						itemGroup.onItemCheckedChangeListener?.onItemCheckedChange(item)
					}
				}
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
					if (item.onClickListener != null) {
						item.onClickListener?.onClick(it)
					}
				}
			}
			LIST_ITEM_ICON -> {
				val tempHolder = holder as IconWithTextHolder
				tempHolder.iconImageView.setImageDrawable(item.icon?.let { context.getDrawable(it) })
				tempHolder.titleTextView.text = item.title

				if (item.onClickListener != null) {
					holder.itemView.setOnClickListener(item.onClickListener)
				}
			}
			LIST_ITEM_ICON_CHECKBOX -> {
				val tempHolder = holder as IconWithTextWithCheckboxHolder
				tempHolder.iconImageView.setImageDrawable(item.icon?.let { context.getDrawable(it) })
				tempHolder.titleTextView.text = item.title
				tempHolder.checkBox.isChecked = itemGroup?.selected?.contains(item) ?: false
				holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
					if (isChecked && itemGroup?.selected?.contains(item) == false) {
						itemGroup.selected.add(item)
					} else {
						itemGroup?.selected?.remove(item)
					}
					// Handle on item checked listener in item's group
					if (itemGroup?.onItemCheckedChangeListener != null) {
						itemGroup.onItemCheckedChangeListener?.onItemCheckedChange(item)
					}
				}
				holder.itemView.setOnClickListener {
					if (itemGroup?.selected?.contains(item) == true) {
						itemGroup.selected.remove(item)
					} else {
						itemGroup?.selected?.add(item)
					}
					tempHolder.checkBox.isChecked = itemGroup?.selected?.contains(item) ?: false

					// Handle on item checked listener in item's group
					if (itemGroup?.onItemCheckedChangeListener != null) {
						itemGroup.onItemCheckedChangeListener?.onItemCheckedChange(item)
					}

					// Handle on click listener in item
					if (item.onClickListener != null) {
						item.onClickListener?.onClick(it)
					}
				}
			}
			LIST_ITEM_ICON_RADIO_BUTTON -> {
				val tempHolder = holder as IconWithTextWithRadioButtonHolder
				tempHolder.iconImageView.setImageDrawable(item.icon?.let { context.getDrawable(it) })
				tempHolder.titleTextView.text = item.title
				tempHolder.radioButton.isChecked = itemGroup?.selected?.contains(item) ?: false
				holder.radioButton.setOnCheckedChangeListener { _, isChecked ->
					if (isChecked && itemGroup?.selected?.contains(item) == false) {
						// Clear any existing selected items
						if (itemGroup.selected.size > 0) {
							// Update state of items that are not the item that was clicked
							itemGroup.selected.forEach { item ->
								if (items.indexOf(item) != -1) {
									notifyItemChanged(items.indexOf(item))
								}
							}
							itemGroup.selected.clear()
						}
						itemGroup.selected.add(item)
					} else {
						itemGroup?.selected?.remove(item)
					}
					// Handle on item checked listener in item's group
					if (itemGroup?.onItemCheckedChangeListener != null) {
						itemGroup.onItemCheckedChangeListener?.onItemCheckedChange(item)
					}
				}
				holder.itemView.setOnClickListener {
					if (itemGroup?.selected?.contains(item) == true) {
						itemGroup.selected.remove(item)
					} else {
						// Clear any existing selected items
						if (itemGroup?.selected?.size!! > 0) {
							Log.d(TAG, "Current list of selected items: ${itemGroup.selected}")
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
					if (item.onClickListener != null) {
						item.onClickListener?.onClick(it)
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
			checkBox.visibility = View.VISIBLE
		}
	}

	inner class NoIconWithTextWithCheckboxHolder(itemView: View) : NoIconWithTextHolder(itemView) {
		internal var checkBox: CheckBox = itemView.findViewById(R.id.itemCheckBox)

		init {
			checkBox.visibility = View.VISIBLE
		}
	}

	inner class IconWithTextWithRadioButtonHolder(itemView: View) : IconWithTextHolder(itemView) {
		internal var radioButton: RadioButton = itemView.findViewById(R.id.itemRadioButton)

		init {
			radioButton.visibility = View.VISIBLE
		}
	}

	inner class NoIconWithTextWithRadioButtonHolder(itemView: View) : NoIconWithTextHolder(itemView) {
		internal var radioButton: RadioButton = itemView.findViewById(R.id.itemRadioButton)

		init {
			radioButton.visibility = View.VISIBLE
		}
	}

	/**
	 * Listener for when an item's checked state is toggled
	 */
	interface OnItemCheckedChangeListener {
		/**
		 * Called when a item's checked state is toggled
		 * @param item The item whose checked state was toggled
		 */
		fun onItemCheckedChange(item: ModalBottomSheetItem)
	}

	companion object {
		private const val LIST_ITEM_NO_ICON = 0
		private const val LIST_ITEM_NO_ICON_CHECKBOX = 1
		private const val LIST_ITEM_NO_ICON_RADIO_BUTTON = 2
		private const val LIST_ITEM_ICON = 3
		private const val LIST_ITEM_ICON_CHECKBOX = 4
		private const val LIST_ITEM_ICON_RADIO_BUTTON = 5
		private val TAG = SharedUtils.getTag(ModalBottomSheetAdapter::class.java)
	}
}