package com.edricchan.studybuddy.features.tasks.domain.repo.project

import androidx.paging.PagingData
import com.edricchan.studybuddy.features.tasks.domain.model.TaskProject
import com.edricchan.studybuddy.features.tasks.domain.model.create.CreateTaskProjectInput
import kotlinx.coroutines.flow.Flow

interface TaskProjectRepository {
    /** Gets the project by its [id] as a [Flow]. */
    fun observeProjectById(id: String): Flow<TaskProject?>

    /**
     * Gets a paginated list of projects.
     * @param config Configuration options specifying how the items should be paginated,
     * as well as filtering options. (See [TaskProjectsPaginationConfig])
     */
    fun observeProjects(config: TaskProjectsPaginationConfig): Flow<PagingData<TaskProject>>

    /** Adds the specified [project][input] to the database. */
    suspend fun addProject(input: CreateTaskProjectInput)

    /** Updates the specified [project][id] with the given [valueMap]. */
    suspend fun updateProject(id: String, valueMap: Map<TaskProject.Field, Any?>)

    /** Updates the specified [project][id] with the given list of [values]. */
    suspend fun updateProject(id: String, vararg values: TaskProject.FieldValue<*>) {
        updateProject(id = id, valueMap = values.associate { it.field to it.value })
    }

    /** Deletes the specified [project][id] from the database. */
    suspend fun deleteProjectById(id: String)

    /** Deletes the specified [project] from the database. */
    suspend fun deleteProject(project: TaskProject) {
        deleteProjectById(project.id)
    }

    /** Bulk deletes the specified [projects][projectIds] from the database. */
    suspend fun deleteProjectsById(projectIds: Set<String>)

    /** Bulk deletes the specified [projects][projects] from the database. */
    suspend fun deleteProjects(projects: Set<TaskProject>) {
        deleteProjectsById(projects.map { it.id }.toSet())
    }
}
