package com.edricchan.studybuddy.adapter.itemdetails;

import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;

public class TaskItemDetails extends ItemDetailsLookup.ItemDetails<String> {
	private final int adapterPosition;
	private final String selectionKey;

	/**
	 * Creates a new {@link androidx.recyclerview.selection.ItemDetailsLookup.ItemDetails<String>}
	 *
	 * @param adapterPosition The adapter position of this item
	 * @param selectionKey    The selection key
	 */
	public TaskItemDetails(int adapterPosition, String selectionKey) {
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

	@Override
	public boolean inSelectionHotspot(@NonNull MotionEvent e) {
		return true;
	}
}
