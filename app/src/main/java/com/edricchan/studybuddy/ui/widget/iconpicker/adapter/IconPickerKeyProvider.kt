package com.edricchan.studybuddy.ui.widget.iconpicker.adapter

import androidx.recyclerview.selection.ItemKeyProvider
import com.edricchan.studybuddy.interfaces.chat.icon.ChatIcon
import java.util.*

class IconPickerKeyProvider(
		private val items: List<ChatIcon>
) : ItemKeyProvider<String>(SCOPE_CACHED) {
	private val keyToPosition: MutableMap<String?, Int>

	init {
		keyToPosition = HashMap(items.size)
		for ((i, item) in items.withIndex()) {
			keyToPosition[item.id] = i
		}

	}

	override fun getKey(position: Int): String? {
		return items[position].id
	}

	override fun getPosition(key: String): Int {
		return keyToPosition[key]!!
	}
}