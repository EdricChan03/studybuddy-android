package com.edricchan.studybuddy.data.paging.firestore

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import kotlin.reflect.KClass

// Code from
// https://medium.com/firebase-tips-tricks/how-to-paginate-firestore-using-paging-3-on-android-c485acb0a2df
class FirestorePagingSource<T : Any>(
    private val query: Query,
    private val limit: Long,
    private val clazz: KClass<T>,
) : PagingSource<QuerySnapshot, T>() {

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, T> {
        try {
            val currentPage = params.key ?: query.limit(limit).get().await()
            Log.i("FireStorePagingSource", "current size: ${currentPage.size()}")

            return LoadResult.Page(
                data = currentPage.toObjects(clazz.java),
                prevKey = getPrevPage(currentPage),
                nextKey = getNextPage(currentPage)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<QuerySnapshot, T>): QuerySnapshot? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    private suspend fun getPrevPage(current: QuerySnapshot): QuerySnapshot? {
        if (!current.isEmpty) {
            val firstVisible = current.documents.first()
            val prevPage = query
                .endBefore(firstVisible)
                .limitToLast(limit)
                .get()
                .await()
            return if (prevPage.isEmpty) null else prevPage
        }
        return null
    }

    private suspend fun getNextPage(current: QuerySnapshot): QuerySnapshot? {
        if (!current.isEmpty) {
            val lastVisible = current.documents.last()
            val nextPage = query
                .startAfter(lastVisible)
                .limit(limit)
                .get()
                .await()
            return if (nextPage.isEmpty) null else nextPage
        }
        return null
    }
}

inline fun <reified T : Any> firestorePagingSource(
    query: Query,
    limit: Long
): FirestorePagingSource<T> = FirestorePagingSource(
    query = query,
    limit = limit,
    clazz = T::class
)
