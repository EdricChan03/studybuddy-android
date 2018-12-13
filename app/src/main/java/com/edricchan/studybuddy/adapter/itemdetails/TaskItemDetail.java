package com.edricchan.studybuddy.adapter.itemdetails;

import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;

public class TaskItemDetail extends ItemDetailsLookup.ItemDetails<String> {
	private final int adapterPosition;
	private final String selectionKey;

	/**
	 * Creates a new {@link androidx.recyclerview.selection.ItemDetailsLookup.ItemDetails<String>}
	 *
	 * @param adapterPosition The adapter position of this item
	 * @param selectionKey    The selection key
	 */
	public TaskItemDetail(int adapterPosition, String selectionKey) {
		this.adapterPosition = adapterPosition;
		this.selectionKey = selectionKey;
	}

	@Override
	public int getPosition() {
		return adapterPosition;
	}

	@Nullable
	@Override
	public String getSelectionKey() {
		return selectionKey;
	}
}
