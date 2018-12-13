package com.edricchan.studybuddy.adapter.itemkeyprovider;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemKeyProvider;

import com.edricchan.studybuddy.interfaces.TaskItem;

import java.util.List;
import java.util.stream.IntStream;

import java8.util.stream.IntStreams;

public class TaskItemKeyProvider extends ItemKeyProvider<String> {

	private final List<TaskItem> itemList;

	/**
	 * Creates a new provider with the given scope.
	 *
	 * @param scope Scope can't be changed at runtime.
	 */
	public TaskItemKeyProvider(@Scope int scope, List<TaskItem> itemList) {
		super(scope);
		this.itemList = itemList;
	}

	@Nullable
	@Override
	public String getKey(int position) {
		return itemList.get(position).getId();
	}

	@Override
	public int getPosition(@NonNull String key) {
		return getTaskItemPosition(itemList, key);
	}

	/**
	 * Gets the position of the first occurrence of the document ID
	 *
	 * @param list The list to get the position for
	 * @param id   The ID of the document
	 * @return The position of the first occurrence of the document ID
	 */
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
