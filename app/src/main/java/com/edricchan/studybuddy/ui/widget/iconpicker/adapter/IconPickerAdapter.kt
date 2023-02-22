package com.edricchan.studybuddy.ui.widget.iconpicker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.load
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.interfaces.chat.icon.ChatIcon

class IconPickerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    /**
     * The info button's on click listener
     */
    var infoButtonOnClickListener: ((chatIcon: ChatIcon) -> Unit)? = null
    var items: List<ChatIcon> = listOf()
    var tracker: SelectionTracker<Long>? = null
    private var itemType: Int = HolderLayout.LIST.itemType

    override fun getItemViewType(position: Int): Int {
        return itemType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HolderLayout.GRID.itemType -> GridHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_grid_icon, parent, false)
            )

            else -> ListHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_list_icon, parent, false)
            )
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (getItemViewType(position)) {
            HolderLayout.LIST.itemType -> {
                (holder as ListHolder).apply {
                    if (tracker != null) {
                        itemView.isActivated = tracker?.isSelected(getItemId(position))
                            ?: false
                    }

                    iconImageView.apply {
                        if (!item.assets.isNullOrEmpty() && tracker?.isSelected(
                                getItemId(position)
                            ) == false
                        ) {
                            // See https://stackoverflow.com/a/35306315/6782707 for more info
                            load(item.assets[0].src) {
                                placeholder(CircularProgressDrawable(context).apply {
                                    strokeWidth = 2f
                                    centerRadius = 12f
                                    start()
                                })
                            }
                        } else if (tracker?.isSelected(getItemId(position)) == true) {
                            setImageResource(R.drawable.ic_check_24dp)
                        } else {
                            setImageResource(R.drawable.ic_forum_outline_24dp)
                        }
                    }

                    nameTextView.text = item.name
                    descTextView.text = item.description

                    if (infoButtonOnClickListener != null) {
                        infoImageButton.setOnClickListener {
                            infoButtonOnClickListener?.invoke(item)
                        }
                    }
                }
            }
        }
    }

    /**
     * Sets the ViewHolder layout to use.
     * @param layout The layout to use.
     * @see HolderLayout
     */
    fun setHolderLayout(layout: HolderLayout) {
        itemType = layout.itemType
    }

    enum class HolderLayout(val itemType: Int) {
        /**
         * Represents that a list layout should be used
         */
        LIST(HOLDER_LAYOUT_LIST_ITEM_TYPE),

        /**
         * Represents that a grid layout should be used
         */
        GRID(HOLDER_LAYOUT_GRID_ITEM_TYPE)
    }

    open class Holder(view: View) : RecyclerView.ViewHolder(view) {
        internal val iconImageView: ImageView = view.findViewById(R.id.iconImageView)
        internal val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        internal val descTextView: TextView = view.findViewById(R.id.descTextView)

        val itemDetails = IconPickerItemDetailsLookup.IconPickerItemDetails(
            bindingAdapterPosition,
            itemId
        )
    }

    class ListHolder(view: View) : Holder(view) {
        internal val infoImageButton: ImageButton = view.findViewById(R.id.infoImageButton)
    }

    class GridHolder(view: View) : Holder(view)

    companion object {
        /** The numerical value of [HolderLayout.LIST.itemType]. */
        const val HOLDER_LAYOUT_LIST_ITEM_TYPE = 0

        /** The numerical value of [HolderLayout.GRID.itemType]. */
        const val HOLDER_LAYOUT_GRID_ITEM_TYPE = 1
    }
}
