package com.edricchan.studybuddy.ui.modules.chat.adapter

import androidx.recyclerview.selection.ItemKeyProvider
import com.edricchan.studybuddy.interfaces.chat.Chat

class ChatItemKeyProvider(
    private val chatList: List<Chat>
) : ItemKeyProvider<String>(SCOPE_CACHED) {
    private val keyToPosition: MutableMap<String?, Int>

    init {
        keyToPosition = HashMap(chatList.size)
        var i = 0
        for ((id) in chatList) {
            keyToPosition[id] = i
            i++
        }

    }

    override fun getKey(position: Int): String {
        return chatList[position].id
    }

    override fun getPosition(key: String): Int {
        return keyToPosition[key]!!
    }
}
