package com.edricchan.studybuddy.ui.widget.iconpicker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.extensions.TAG
import com.edricchan.studybuddy.extensions.firebase.toDateFormat
import com.edricchan.studybuddy.extensions.toDateFormat
import com.edricchan.studybuddy.interfaces.chat.icon.ChatIcon
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class IconPickerInfoBottomSheetFragment : BottomSheetDialogFragment() {
    /**
     * The icon to be used to retrieve information about the icon.
     */
    var icon: ChatIcon? = null

    private lateinit var fragmentView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frag_icon_info_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentView = view
        if (icon != null) {
            updateInfo()
        }
    }

    fun updateInfoName(view: View, name: String? = null) {
        val tempName = icon?.name ?: name
        if (tempName != null) {
            updateTextViewText(view, R.id.iconNameValue, tempName)
        } else {
            Log.w(TAG, "No name specified for icon info.")
        }
    }

    fun updateInfoDesc(view: View, desc: String? = null) {
        val tempDesc = icon?.description ?: desc
        if (tempDesc != null) {
            updateTextViewText(view, R.id.iconDescValue, tempDesc)
        } else {
            Log.w(TAG, "No description specified for icon info.")
        }
    }

    fun updateInfoAuthor(
        view: View, author: Any? = null,
        firestore: FirebaseFirestore? = FirebaseFirestore.getInstance()
    ) {
        val tempAuthor = icon?.author ?: author
        // Whether the value is a document reference
        val isFirebaseUser = tempAuthor is DocumentReference
        val isString = tempAuthor is String
        if (isFirebaseUser) {
            if (firestore != null) {
                (tempAuthor as DocumentReference).get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful && task.result != null) {
                            val authorResult = task.result
                            if (authorResult?.contains("displayName") == true) {
                                updateTextViewText(
                                    view,
                                    R.id.iconAuthorValue,
                                    authorResult.get("displayName") as String
                                )
                            } else {
                                Log.w(TAG, "Author document reference has no display name!")
                            }
                        } else {
                            Log.e(
                                TAG,
                                "An error occurred while attempting to retrieve the author's data:",
                                task.exception
                            )
                        }
                    }
            } else {
                Log.e(
                    TAG, "An instance of FirebaseFirestore is required to call this method " +
                            "if the author parameter is a document reference."
                )
            }
        } else if (isString) {
            updateTextViewText(view, R.id.iconAuthorValue, tempAuthor as String)
        } else {
            Log.w(
                TAG, "Invalid data type entered for author parameter. " +
                        "(Valid data types: String, DocumentReference)"
            )
        }
    }

    fun updateInfoAuthorInfo(view: View, authorInfo: String? = null) {
        val tempAuthorInfo = icon?.authorInfo ?: authorInfo
        if (tempAuthorInfo != null) {
            updateTextViewText(view, R.id.iconDescValue, tempAuthorInfo)
        } else {
            Log.w(TAG, "No author information specified for icon info.")
        }
    }

    fun updateInfoCreatedAt(view: View, createdAt: Any? = null) {
        when (val tempCreatedAt = icon?.createdAt ?: createdAt) {
            is Timestamp -> {
                updateTextViewText(
                    view, R.id.iconDateCreatedAtValue,
                    tempCreatedAt.toDateFormat(getString(R.string.date_format_pattern))
                )
            }
            is Date -> {
                updateTextViewText(
                    view, R.id.iconDateCreatedAtValue,
                    tempCreatedAt.toDateFormat(getString(R.string.date_format_pattern))
                )
            }
            is Long -> {
                updateTextViewText(
                    view, R.id.iconDateCreatedAtValue,
                    tempCreatedAt.toDateFormat(getString(R.string.date_format_pattern))
                )
            }
            is String -> {
                updateTextViewText(view, R.id.iconDateCreatedAtValue, tempCreatedAt)
            }
            else -> Log.w(TAG, "Invalid data type entered for created at timestamp.")
        }
    }

    fun updateInfoLastModified(view: View, lastModified: Any? = null) {
        when (val tempLastModified = icon?.lastModified ?: lastModified) {
            is Timestamp -> {
                updateTextViewText(
                    view, R.id.iconDateLastModifiedValue,
                    tempLastModified.toDateFormat(getString(R.string.date_format_pattern))
                )
            }
            is Date -> {
                updateTextViewText(
                    view, R.id.iconDateLastModifiedValue,
                    tempLastModified.toDateFormat(getString(R.string.date_format_pattern))
                )
            }
            is Long -> {
                updateTextViewText(
                    view, R.id.iconDateLastModifiedValue,
                    tempLastModified.toDateFormat(getString(R.string.date_format_pattern))
                )
            }
            is String -> {
                updateTextViewText(view, R.id.iconDateLastModifiedValue, tempLastModified)
            }
            else -> Log.w(TAG, "Invalid data type entered for last modified timestamp.")
        }
    }

    /**
     * Updates the current information with the specified [icon], if any
     * @param view The view to query the resource IDs from
     * @param icon The icon, if any (Note: If this is not specified, it is defaulted to
     * use the [IconPickerInfoBottomSheetFragment.icon] variable.)
     */
    fun updateInfo(view: View? = null, icon: ChatIcon? = null) {
        val tempView = view ?: fragmentView
        val tempIcon = icon ?: this.icon
        updateInfoName(tempView, tempIcon?.name)
        updateInfoDesc(tempView, tempIcon?.description)
        updateInfoAuthor(tempView, tempIcon?.author)
        updateInfoAuthorInfo(tempView, tempIcon?.authorInfo)
        updateInfoCreatedAt(tempView, tempIcon?.createdAt)
        updateInfoLastModified(tempView, tempIcon?.lastModified)
    }

    private fun updateTextViewText(view: View, @IdRes viewResId: Int = -1, text: String) {
        if (viewResId != -1) {
            if (view.findViewById<View>(viewResId) is TextView) {
                // The view is a TextView
                view.findViewById<TextView>(viewResId).text = text
            } else {
                Log.w(TAG, "Could not find resource ID in the view.")
            }
        } else {
            if (view is TextView) {
                view.text = text
            } else {
                Log.w(TAG, "The view specified is not a TextView.")
            }
        }
    }
}