package com.edricchan.studybuddy.ui.modules.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.databinding.ItemChatBinding
import com.edricchan.studybuddy.interfaces.chat.Chat

/**
 * Adapter class to represent a list of chats
 * @property chatList The list of chats
 * @property onItemClickListener An optional listener which is invoked when the item is clicked on
 * @property onItemLongClickListener An optional listener which is invoked when the item is long clicked on
 */
class ChatsAdapter(
    private val chatList: List<Chat>,
    private val onItemClickListener: ((item: Chat) -> Unit)? = null,
    private val onItemLongClickListener: ((item: Chat) -> Boolean)? = null
) : RecyclerView.Adapter<ChatsAdapter.ViewHolder>() {
    var selectionTracker: SelectionTracker<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemChatBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemCount(): Int = chatList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(chatList[position])
    }

    inner class ViewHolder(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Chat) {
            binding.apply {
                if (!item.name.isNullOrEmpty()) {
                    nameTextView.text = item.name
                } else {
                    nameTextView.setText(R.string.frag_chat_default_group_name)
                }

                if (!item.description.isNullOrEmpty()) {
                    descTextView.text = item.description
                } else {
                    descTextView.setText(R.string.frag_chat_default_group_desc)
                }

                if (item.icon != null) {
                    // Reset to group pic style
                    iconImageView.apply {
                        setImageDrawable(null)

                        load(item.icon) {
                            transformations(CircleCropTransformation())
                            fallback(R.drawable.ic_forum_outline_24dp)
                        }
                    }
                } else {
                    // Reset to no group pic style
                    iconImageView.setImageResource(R.drawable.ic_forum_outline_24dp)
                }

                itemView.setOnClickListener {
                    if (selectionTracker?.hasSelection() == false) {
                        onItemClickListener?.invoke(item)
                    }
                }
                itemView.setOnLongClickListener {
                    if (selectionTracker?.hasSelection() == false) {
                        onItemLongClickListener?.invoke(item) ?: false
                    } else {
                        false
                    }
                }

                if (selectionTracker != null && selectionTracker?.hasSelection() == true) {
                    selectionCheckBox.isInvisible = false
                    iconImageView.isInvisible = true
                    itemView.isActivated = selectionTracker!!.isSelected(item.id)
                } else {
                    selectionCheckBox.isInvisible = true
                    iconImageView.isInvisible = false
                }
            }
        }

        val itemDetails = ChatItemDetailsLookup.ChatItemDetails(
            bindingAdapterPosition,
            chatList[bindingAdapterPosition].id
        )
    }
}
