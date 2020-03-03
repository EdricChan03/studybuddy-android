package com.edricchan.studybuddy.extensions.firebase.firestore

import com.edricchan.studybuddy.interfaces.HasId
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects

/**
 * Converts a query document snapshot value to its object equivalent with the document's ID attached
 * @param T A data class to convert the document snapshot's value to.
 * Note that the data class specified must implement the [HasId] interface.
 * @param overwriteModelId Whether to overwrite the model's ID with the document ID
 * @see HasId
 */
@Deprecated("Use the DocumentId annotation included in version 20.2.0 of Firestore.")
inline fun <reified T : HasId> QueryDocumentSnapshot.toObjectWithId(overwriteModelId: Boolean = false): T {
    val model = this.toObject<T>()
    if (overwriteModelId) model.id = this.id
    return model
}


/**
 * Converts a document snapshot value to its object equivalent with the document's ID attached
 * @param T A data class to convert the document snapshot's value to.
 * Note that the data class specified must implement the [HasId] interface.
 * @param overwriteModelId Whether to overwrite the model's ID with the document ID
 * @see HasId
 */
@Deprecated("Use the DocumentId annotation included in version 20.2.0 of Firestore.")
inline fun <reified T : HasId> DocumentSnapshot.toObjectWithId(overwriteModelId: Boolean = false): T? {
    val model = this.toObject<T>()
    if (overwriteModelId) model?.id = this.id
    return model
}

/**
 * Converts a query snapshot's documents to its object equivalent with the document's ID attached
 * @param T A data class to convert the document snapshot's value to.
 * Note that the data class specified must implement the [HasId] interface.
 * @param overwriteModelId Whether to overwrite the model IDs with the document ID
 * @see HasId
 */
@Deprecated("Use the DocumentId annotation included in version 20.2.0 of Firestore.")
inline fun <reified T : HasId> QuerySnapshot.toObjectsWithId(overwriteModelId: Boolean = false): List<T> {
    val models = this.toObjects<T>().toMutableList()
    if (overwriteModelId) {
        this.documents.forEachIndexed { index, documentSnapshot ->
            models[index].id = documentSnapshot.id
        }
    }
    return models
}
