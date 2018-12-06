package com.edricchan.studybuddy.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.edricchan.studybuddy.R;
import com.edricchan.studybuddy.interfaces.TaskProject;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TaskProjectSpinnerAdapter extends ArrayAdapter<TaskProject> {

	private Context context;
	private List<TaskProject> projects;

	/**
	 * {@inheritDoc}
	 */
	public TaskProjectSpinnerAdapter(@NonNull Context context, int textViewResourceId, List<TaskProject> projects) {
		super(context, textViewResourceId, projects);
		this.context = context;
		this.projects = projects;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getCount() {
		int count = super.getCount();
		return count > 0 ? count - 1 : count;
	}

	/**
	 * {@inheritDoc}
	 */
	@Nullable
	@Override
	public TaskProject getItem(int position) {
		return projects.get(position);
	}

	/**
	 * Retrieves a task project at the <code>position</code> specified
	 *
	 * @param position The position of the task project
	 * @deprecated Use {@link TaskProjectSpinnerAdapter#getItem(int)} instead
	 */
	public TaskProject getTaskProject(int position) {
		return getItem(position);
	}

	/**
	 * Retrieves the ID of a task project
	 *
	 * @param position The position of the task project in the Spinner's view
	 * @return The task project's ID
	 */
	public String getTaskProjectId(int position) {
		return projects.get(position).id;
	}

	/**
	 * Retrieves a task project by it's ID
	 *
	 * @param id The ID to search for
	 * @return The task project
	 */
	public TaskProject getTaskProjectById(String id) {
		for (TaskProject project : projects) {
			if (project.id.equals(id)) {
				return project;
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@NonNull
	@Override
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {
		TextView label = (TextView) super.getView(position, convertView, parent);
		if (position == getCount()) {
			label.setText("");
			label.setHint(R.string.task_project_prompt);
		} else {
			label.setText(projects.get(position).name);
		}
		return label;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getDropDownView(int position, View convertView,
	                            @NonNull ViewGroup parent) {
		TextView label = (TextView) super.getDropDownView(position, convertView, parent);
		label.setText(projects.get(position).name);

		return label;
	}
}
