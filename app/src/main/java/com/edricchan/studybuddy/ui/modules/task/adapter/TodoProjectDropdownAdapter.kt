package com.edricchan.studybuddy.ui.modules.task.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.interfaces.TodoProject

class TodoProjectDropdownAdapter(
    context: Context,
    textViewResourceId: Int,
    private val projects: List<TodoProject>
) : ArrayAdapter<TodoProject>(context, textViewResourceId, projects) {

    /*override fun getCount(): Int {
        val count = super.getCount()
        return if (count > 0) count else count
    }*/

    override fun getItem(position: Int): TodoProject? {
        return projects[position]
    }

    /**
     * Retrieves a task project at the `position` specified
     *
     * @param position The position of the task project
     */
    @Deprecated("Use TodoProjectDropdownAdapter#getItem(int) instead")
    fun getTaskProject(position: Int): TodoProject? {
        return getItem(position)
    }

    /**
     * Retrieves the ID of a task project
     *
     * @param position The position of the task project in the Spinner's view
     * @return The task project's ID
     */
    fun getTaskProjectId(position: Int): String? {
        return projects[position].id
    }

    /**
     * Retrieves a task project by it's ID
     *
     * @param id The ID to search for
     * @return The task project
     */
    fun getTaskProjectById(id: String): TodoProject? {
        for (project in projects) {
            if (project.id == id) {
                return project
            }
        }
        return null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = super.getView(position, convertView, parent) as TextView
        if (position == count) {
            label.text = ""
            label.setHint(R.string.task_project_choose_prompt_text)
        } else {
            label.text = projects[position].name
        }
        return label
    }

    override fun getDropDownView(
        position: Int, convertView: View?,
        parent: ViewGroup
    ): View {
        val label = super.getDropDownView(position, convertView, parent) as TextView
        label.text = projects[position].name

        return label
    }
}
