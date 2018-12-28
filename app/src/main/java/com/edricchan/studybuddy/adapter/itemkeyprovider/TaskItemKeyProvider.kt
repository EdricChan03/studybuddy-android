package com.edricchan.studybuddy.adapter.itemkeyprovider

import android.os.Build
import androidx.recyclerview.selection.ItemKeyProvider
import com.edricchan.studybuddy.SharedHelper
import com.edricchan.studybuddy.interfaces.TaskItem
import java8.util.stream.IntStreams
import java.util.*
import java.util.stream.IntStream

/**
 * Creates a new provider.
 *
 * @param itemList The list of tasks
 */
class TaskItemKeyProvider(
		private val mItemList: List<TaskItem>
) : ItemKeyProvider<String>(ItemKeyProvider.SCOPE_CACHED) {
	private val mKeyToPosition: MutableMap<String, Int>

	init {
		this.mKeyToPosition = HashMap(mItemList.size)
		var i = 0
		for ((_, _, _, id) in mItemList) {
			mKeyToPosition[id!!] = i
			i++
		}

	}

	override fun getKey(position: Int): String? {
		return mItemList[position].id
	}

	override fun getPosition(key: String): Int {
		return mKeyToPosition[key]!!
	}

	/**
	 * Gets the position of the first occurrence of the document ID
	 *
	 * @param list The list to get the position for
	 * @param id   The ID of the document
	 * @return The position of the first occurrence of the document ID
	 */
	@Deprecated("")
	private fun getTaskItemPosition(list: List<TaskItem>, id: String): Int {
		return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			IntStream.range(0, list.size)
					.filter { index -> list[index].id == id }
					.findFirst()
					.orElse(-1)
		} else {
			IntStreams
					.range(0, list.size)
					.filter { index -> list[index].id == id }
					.findFirst()
					.orElse(-1)
		}
	}

	companion object {
		private val TAG = SharedHelper.getTag(TaskItemKeyProvider::class.java)
	}
}
