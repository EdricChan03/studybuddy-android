package com.edricchan.studybuddy.adapter.predicate;

import androidx.annotation.NonNull;
import androidx.recyclerview.selection.SelectionTracker;

public class TaskItemPredicate extends SelectionTracker.SelectionPredicate<String> {
	@Override
	public boolean canSetStateForKey(@NonNull String key, boolean nextState) {
		return true;
	}

	@Override
	public boolean canSetStateAtPosition(int position, boolean nextState) {
		return true;
	}

	@Override
	public boolean canSelectMultiple() {
		return true;
	}
}
