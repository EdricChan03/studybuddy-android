package com.edricchan.studybuddy;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by edricchan on 2/8/17.
 */
public class TaskItem {
	public String title;
	public String content;
	public List<String> projects;
	public List<String> tags;
	public Timestamp dueDate;
	public String id;
	public boolean hasDone;

	public TaskItem() {
	}

	public TaskItem(String title, String content) {
		this.title = title;
		this.content = content;
		this.hasDone = false;
	}

	public TaskItem(String title, String content, List<String> projects) {
		this.title = title;
		this.content = content;
		this.projects = projects;
		this.hasDone = false;
	}

	public TaskItem(String title, String content, String[] projects) {
		this.title = title;
		this.content = content;
		this.projects = Arrays.asList(projects);
		this.hasDone = false;
	}

	public TaskItem(String title, String content, boolean hasDone) {
		this.title = title;
		this.content = content;
		this.hasDone = hasDone;
		this.projects = new ArrayList<>();
	}

	public TaskItem(String title, String content, List<String> projects, Timestamp dueDate, boolean hasDone) {
		this.title = title;
		this.content = content;
		this.projects = projects;
		this.dueDate = dueDate;
		this.hasDone = hasDone;
	}

	public TaskItem(String title, String content, List<String> projects, Date dueDate, boolean hasDone) {
		this.title = title;
		this.content = content;
		this.projects = projects;
		this.dueDate = new Timestamp(dueDate);
		this.hasDone = hasDone;
	}

	public TaskItem(String title, String content, String[] projects, Timestamp dueDate, boolean hasDone) {
		this.title = title;
		this.content = content;
		this.projects = Arrays.asList(projects);
		this.dueDate = dueDate;
		this.hasDone = hasDone;
	}

	public TaskItem(String title, String content, String[] projects, Date dueDate, boolean hasDone) {
		this.title = title;
		this.content = content;
		this.projects = Arrays.asList(projects);
		this.dueDate = new Timestamp(dueDate);
		this.hasDone = hasDone;
	}

	public TaskItem(String title, String content, List<String> projects, List<String> tags, Timestamp dueDate, boolean hasDone) {
		this.title = title;
		this.content = content;
		this.projects = projects;
		this.tags = tags;
		this.dueDate = dueDate;
		this.hasDone = hasDone;
	}

	public TaskItem(String title, String content, List<String> projects, List<String> tags, Date dueDate, boolean hasDone) {
		this.title = title;
		this.content = content;
		this.projects = projects;
		this.tags = tags;
		this.dueDate = new Timestamp(dueDate);
		this.hasDone = hasDone;
	}

	public TaskItem(String title, String content, String[] projects, String[] tags, Timestamp dueDate, boolean hasDone) {
		this.title = title;
		this.content = content;
		this.projects = Arrays.asList(projects);
		this.tags = Arrays.asList(tags);
		this.dueDate = dueDate;
		this.hasDone = hasDone;
	}

	public TaskItem(String title, String content, String[] projects, String[] tags, Date dueDate, boolean hasDone) {
		this.title = title;
		this.content = content;
		this.projects = Arrays.asList(projects);
		this.tags = Arrays.asList(tags);
		this.dueDate = new Timestamp(dueDate);
		this.hasDone = hasDone;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setProjects(List<String> projects) {
		this.projects = projects;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public void setDueDate(Timestamp dueDate) {
		this.dueDate = dueDate;
	}

	public void setHasDone(boolean hasDone) {
		this.hasDone = hasDone;
	}

	public void setId(String id) {
		this.id = id;
	}

}
