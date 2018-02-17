package com.edricchan.studybuddy;

import java.util.Date;

/**
 * Created by edricchan on 2/8/17.
 */

public class TaskItem {
	private String title;
	private String project;
	private String content;
	private Date dueDate;
	public TaskItem(String title, Date dueDate, String project, String content) {
		this.title = title;
		this.dueDate = dueDate;
		this.project = project;
		this.content = content;
	}
	/**
	 * Gets the task name.
	 * @return The name of the task in a string format.
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * Sets the task name.
	 * @param title The task name.
	 */
	public void setTitle(String title) {
		this.title  = title;
	}

	/**
	 * Gets the task due date.
	 * @return The task's due date in a `Date` format.
	 */
	public Date getDueDate() {
		return dueDate;
	}

	/**
	 * Sets the task's due date.
	 * @param dueDate The task's due date. (Date)
	 */
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	/**
	 * Gets the task project.
	 * @return The task's project in a <code>string</code> format.
	 */
	public String getProject() {
		return project;
	}
	/**
	 * Sets the task project.
	 * @param project The task's project. (String)
	 */
	public void setProject(String project) {
		this.project = project;
	}
	/**
	 * Gets the task content.
	 * @return The task's content in a <code>string</code> format.
	 */
	public String getContent() {
		return content;
	}
	/**
	 * Sets the task content.
	 * @param content The task's content. (<code>String</code>)
	 */
	public void setContent(String content) {
		this.content = content;
	}
}
