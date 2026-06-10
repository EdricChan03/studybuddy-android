package com.edricchan.studybuddy.features.tasks.data.repo.project

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.edricchan.studybuddy.data.paging.firestore.firestorePagingSource
import com.edricchan.studybuddy.domain.common.sorting.toFirestoreDirection
import com.edricchan.studybuddy.features.tasks.data.mapper.toDomain
import com.edricchan.studybuddy.features.tasks.data.mapper.toDto
import com.edricchan.studybuddy.features.tasks.data.model.TodoProject
import com.edricchan.studybuddy.features.tasks.data.model.create.toDto
import com.edricchan.studybuddy.features.tasks.data.repo.source.TaskProjectDataSource
import com.edricchan.studybuddy.features.tasks.domain.model.TaskProject
import com.edricchan.studybuddy.features.tasks.domain.model.create.CreateTaskProjectInput
import com.edricchan.studybuddy.features.tasks.domain.repo.project.TaskProjectRepository
import com.edricchan.studybuddy.features.tasks.domain.repo.project.TaskProjectsPaginationConfig
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FirebaseTaskProjectRepositoryImpl @Inject constructor(
    private val source: TaskProjectDataSource
) : TaskProjectRepository {
    override fun observeProjectById(id: String): Flow<TaskProject?> {
        return source[id].map { it?.toDomain() }
    }

    override fun observeProjects(config: TaskProjectsPaginationConfig): Flow<PagingData<TaskProject>> =
        flow {
            val query = source.getCollectionRef().let {
                config.orderByFields.entries.fold(it as Query) { query, (field, direction) ->
                    query.orderBy(
                        field.toDto().fieldName,
                        direction.toFirestoreDirection()
                    )
                }
            }

            emitAll(
                Pager(
                    config = PagingConfig(
                        pageSize = config.pageSize
                    )
                ) {
                    firestorePagingSource<TodoProject>(
                        query = query,
                        limit = config.pageSize.toLong()
                    )
                }.flow.map { it.map(TodoProject::toDomain) }
                    .cachedIn(config.cachedCoroutineScope)
            )
        }

    override suspend fun addProject(input: CreateTaskProjectInput) {
        source.add(input.toDto())
    }

    override suspend fun updateProject(
        id: String,
        valueMap: Map<TaskProject.Field, Any?>
    ) {
        source.update(id, valueMap.mapKeys { it.key.toDto().fieldName })
    }

    override suspend fun deleteProjectById(id: String) {
        source.removeById(id)
    }

    override suspend fun deleteProjectsById(projectIds: Set<String>) {
        source.runBatch {
            deleteAll(projectIds)
        }
    }
}
