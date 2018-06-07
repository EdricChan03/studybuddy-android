package com.edricchan.studybuddy;

import java.util.Date;

/**
 * Created by edricchan on 2/8/17.
 */

public class TaskItem {
	public String title;
	public String project;
	public String content;
	public Date dueDate;
	public String id;
	public TaskItem() {

	}
	public void setId(String id) {
		this.id = id;
	}
	public TaskItem(String title, String project, String content, Date dueDate) {
		this.title = title;
		this.project = project;
		this.content = content;
		this.dueDate = dueDate;
	}
	public TaskItem(String title, String project, String content) {
		this.title = title;
		this.project = project;
		this.content = content;
		this.dueDate = new Date();
	}
}
