package com.edricchan.studybuddy.extensions

import com.edricchan.studybuddy.interfaces.HasId
import com.google.firebase.firestore.QueryDocumentSnapshot

/**
 * Converts a document snapshot value to its object equivalent with the document's ID attached
 * @param T A data class to convert the document snapshot's value to. Note that the data class specified must implement the [HasId] interface.
 */
inline fun <reified T : HasId> QueryDocumentSnapshot.toObjectWithId(): T {
	val model = this.toObject(T::class.java)
	model.id = this.id
	return model
}