package com.edricchan.studybuddy.ui.modules.chat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.interfaces.chat.Chat

/**
 * Adapter class to represent a list of chats
 * @property chatList The list of chats
 * @property onItemClickListener An optional listener which is invoked when the item is clicked on
 * @property onItemLongClickListener An optional listener which is invoked when the item is long clicked on
 */
class ChatsAdapter(
    private val context: Context,
    private val chatList: List<Chat>,
    private val onItemClickListener: ((item: Chat) -> Unit)? = null,
    private val onItemLongClickListener: ((item: Chat) -> Boolean)? = null
) : RecyclerView.Adapter<ChatsAdapter.ViewHolder>() {
    var selectionTracker: SelectionTracker<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.item_chat, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = chatList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = chatList[position]

        val itemIconImageView = holder.itemIconImageView
        val itemNameTextView = holder.itemNameTextView
        val itemDescTextView = holder.itemDescTextView
        val itemSelectionCheckBox = holder.itemSelectionCheckbox

        if (item.name != null && item.name.isNotEmpty()) {
            itemNameTextView.text = item.name
        } else {
            itemNameTextView.setText(R.string.frag_chat_default_group_name)
        }

        if (item.description != null && item.description.isNotEmpty()) {
            itemDescTextView.text = item.description
        } else {
            itemDescTextView.setText(R.string.frag_chat_default_group_desc)
        }

        if (item.icon != null) {
            // Reset to group pic style
            itemIconImageView.setImageDrawable(null)

            Glide.with(context)
                .load(item.icon)
                .circleCrop()
                .into(itemIconImageView)
        } else {
            // Reset to no group pic style
            itemIconImageView.setImageResource(R.drawable.ic_forum_outline_24dp)
        }

        holder.itemView.setOnClickListener {
            if (selectionTracker?.hasSelection() == false) {
                onItemClickListener?.invoke(item)
            }
        }
        holder.itemView.setOnLongClickListener {
            if (selectionTracker?.hasSelection() == false) {
                onItemLongClickListener?.invoke(item) ?: false
            } else {
                false
            }
        }

        if (selectionTracker != null && selectionTracker?.hasSelection() == true) {
            itemSelectionCheckBox.visibility = View.VISIBLE
            itemIconImageView.visibility = View.INVISIBLE
            holder.itemView.isActivated = selectionTracker!!.isSelected(item.id)
        } else {
            itemSelectionCheckBox.visibility = View.INVISIBLE
            itemIconImageView.visibility = View.VISIBLE
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal val itemIconImageView: ImageView = view.findViewById(R.id.iconImageView)
        internal val itemNameTextView: TextView = view.findViewById(R.id.nameTextView)
        internal val itemDescTextView: TextView = view.findViewById(R.id.descTextView)
        internal val itemSelectionCheckbox: CheckBox = view.findViewById(R.id.selectionCheckBox)

        fun getItemDetails(): ChatItemDetailsLookup.ChatItemDetails =
            ChatItemDetailsLookup.ChatItemDetails(
                adapterPosition,
                chatList[adapterPosition].id
            )
    }
}