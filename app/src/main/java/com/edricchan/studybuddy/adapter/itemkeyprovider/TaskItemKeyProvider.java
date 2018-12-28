package com.edricchan.studybuddy.adapter.itemkeyprovider;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemKeyProvider;

import com.edricchan.studybuddy.SharedHelper;
import com.edricchan.studybuddy.interfaces.TaskItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import java8.util.stream.IntStreams;

public class TaskItemKeyProvider extends ItemKeyProvider<String> {

	private final List<TaskItem> mItemList;
	private final Map<String, Integer> mKeyToPosition;
	private static final String TAG = SharedHelper.Companion.getTag(TaskItemKeyProvider.class);

	/**
	 * Creates a new provider.
	 *
	 * @param itemList The list of tasks
	 */
	public TaskItemKeyProvider(List<TaskItem> itemList) {
		super(SCOPE_CACHED);
		this.mItemList = itemList;
		this.mKeyToPosition = new HashMap<>(mItemList.size());
		int i = 0;
		for (TaskItem item : itemList) {
			mKeyToPosition.put(item.getId(), i);
			i++;
		}

	}

	@Nullable
	@Override
	public String getKey(int position) {
		return mItemList.get(position).getId();
	}

	@Override
	public int getPosition(@NonNull String key) {
		return mKeyToPosition.get(key);
	}

	/**
	 * Gets the position of the first occurrence of the document ID
	 *
	 * @param list The list to get the position for
	 * @param id   The ID of the document
	 * @return The position of the first occurrence of the document ID
	 */
	@Deprecated
	private int getTaskItemPosition(final List<TaskItem> list, final String id) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			return IntStream.range(0, list.size())
					.filter(index -> list.get(index).getId().equals(id))
					.findFirst()
					.orElse(-1);
		} else {
			return IntStreams
					.range(0, list.size())
					.filter(index -> list.get(index).getId().equals(id))
					.findFirst()
					.orElse(-1);
		}
	}
}
