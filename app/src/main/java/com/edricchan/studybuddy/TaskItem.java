package com.edricchan.studybuddy;

import java.util.Date;

/**
 * Created by edricchan on 2/8/17.
 */

public class TaskItem {
	private String taskName;
	private String[] taskLabels;
	private Date taskDueDate;
	private int taskId;
	public TaskItem(int taskId, String taskName, String[] taskLabels, Date taskDueDate) {
		this.taskId = taskId;
		this.taskName = taskName;
		this.taskLabels = taskLabels;
		this.taskDueDate = taskDueDate;
	}
	/**
	 * Gets the task id
	 * @return The ID of the task in integer format.
	 */
	public int getTaskId() {
		return taskId;
	}
	/**
	 * Sets the task id.
	 * @param taskId The task id.
	 */
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	/**
	 * Gets the task name.
	 * @return The name of the task in a string format.
	 */
	public String getTaskName() {
		return taskName;
	}
	/**
	 * Sets the task name.
	 * @param taskName The task name.
	 */
	public void setTaskName(String taskName) {
		this.taskName  = taskName;
	}

	/**
	 * Gets the task labels.
	 * @return The task labels in an array format.
	 */
	public String[] getTaskLabels() {
		return taskLabels;
	}

	/**
	 * Sets the task labels.
	 * @param taskLabels The task labels. (Array)
	 */
	public void setTaskLabels(String[] taskLabels) {
		this.taskLabels = taskLabels;
	}

	/**
	 * Gets the task due date.
	 * @return The task's due date in a `Date` format.
	 */
	public Date getTaskDueDate() {
		return taskDueDate;
	}

	/**
	 * Sets the task's due date.
	 * @param taskDueDate The task's due date. (Date)
	 */
	public void setTaskDueDate(Date taskDueDate) {
		this.taskDueDate = taskDueDate;
	}
}
