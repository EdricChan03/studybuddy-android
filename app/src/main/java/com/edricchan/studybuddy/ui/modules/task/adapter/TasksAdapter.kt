package com.edricchan.studybuddy.ui.modules.task.adapter

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.extensions.TAG
import com.edricchan.studybuddy.interfaces.TaskItem
import com.edricchan.studybuddy.ui.modules.task.adapter.itemdetails.TaskItemDetails
import com.edricchan.studybuddy.utils.SharedUtils
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import ru.noties.markwon.Markwon

class TasksAdapter : RecyclerView.Adapter<TasksAdapter.Holder> {
    private var mTaskItemList: List<TaskItem>? = null
    private var mContext: Context? = null
    private var mUser: FirebaseUser? = null
    private var mFirestore: FirebaseFirestore? = null
    private var mSnackbarView: View? = null
    private var lastPosition = 1
    private var mItemListener: OnItemClickListener? = null
    private var mUseDeprecatedListener: Boolean? = false

    /**
     * Initialises a new adapter
     *
     * @param context               The context
     * @param taskItemList          The list of items to show
     * @param useDeprecatedListener Whether to use the deprecated listener methods
     * @throws IllegalStateException If `useDeprecatedListener` is set to `true`, this throws as such operations are unsupported.
     */
    @Throws(IllegalStateException::class)
    @JvmOverloads
    constructor(
        context: Context,
        taskItemList: List<TaskItem>,
        useDeprecatedListener: Boolean = false
    ) {
        mTaskItemList = taskItemList
        mContext = context
        if (useDeprecatedListener) {
            throw IllegalStateException("Please use the deprecated method (TasksAdapter#TasksAdapter(Context, List, FirebaseUser, FirebaseFirestore, View)!")
        }
        mUseDeprecatedListener = useDeprecatedListener
    }

    /**
     * Initialises a new adapter
     *
     * @param context      The context
     * @param taskItemList The list of items to show
     * @param user         An instance of the currently logged-in user. (This can be retrieved with [FirebaseAuth.getCurrentUser])
     * @param fs           An instance of [FirebaseFirestore]. (This can be retrieved with [FirebaseFirestore.getInstance])
     * @param snackbarView The view to show a snackbar in
     */
    @Deprecated("Use {@link TasksAdapter#TasksAdapter(Context, List)}")
    constructor(
        context: Context,
        taskItemList: List<TaskItem>,
        user: FirebaseUser,
        fs: FirebaseFirestore,
        snackbarView: View
    ) {
        mTaskItemList = taskItemList
        mContext = context
        mUser = user
        mFirestore = fs
        mSnackbarView = snackbarView
    }

    /**
     * Checks if a string is non-empty
     *
     * @param var The string to check
     * @return True if it is empty, false otherwise
     */
    @Deprecated("Use TextUtils#isEmpty(CharSequence)")
    private fun checkNonEmpty(`var`: String): Boolean {
        return `var`.isNotEmpty()
    }

    /**
     * Checks if a string is not null
     *
     * @param var The string to check
     * @return True if it is not null, false otherwise
     */
    @Deprecated("Use {@link TextUtils#isEmpty(CharSequence)}")
    private fun checkStringNonNull(`var`: String?): Boolean {
        return `var` != null
    }

    /**
     * Checks if a list is not empty
     *
     * @param var The list to check
     * @return True if is not empty, false otherwise
     * @implNote Note that this method doesn't check if the list is null.
     */
    @Deprecated("Use {@link List#isEmpty()}")
    private fun checkNonEmpty(`var`: List<*>): Boolean {
        return `var`.size != 0
    }

    /**
     * Updates the done status
     *
     * @param item     The item
     * @param button   The button to update the text
     * @param position The adapter position
     */
    @Deprecated("This should ideally be left up to the consumer, instead of handling it in the adapter")
    private fun updateDoneStatus(item: TaskItem, button: Button, position: Int) {
        if (item.done!!) {
            mFirestore!!.document("users/" + mUser!!.uid + "/todos/" + item.id)
                .update("done", false).addOnSuccessListener {
                mSnackbarView?.let {
                    Snackbar.make(
                        it,
                        "Task marked as undone.",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
                notifyItemChanged(position)
                button.setText(R.string.action_mark_as_done)
            }
        } else {
            mFirestore!!.document("users/" + mUser!!.uid + "/todos/" + item.id).update("done", true)
                .addOnSuccessListener {
                    mSnackbarView?.let {
                        Snackbar.make(
                            it,
                            "Task marked as done.",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    notifyItemChanged(position)
                    button.setText(R.string.action_mark_as_undone)
                }
        }
    }

    /**
     * Sets the animation of the item view
     *
     * @param viewToAnimate The view to animate
     * @param position      The adapter position
     */
    @Deprecated("This doesn't really work.")
    private fun setAnimation(viewToAnimate: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            val anim = ScaleAnimation(
                0.0f,
                1.0f,
                0.0f,
                1.0f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            )
            anim.duration = 250
            viewToAnimate.startAnimation(anim)
            lastPosition = position
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.taskadapter_item_row, parent, false)
        return Holder(itemView)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = mTaskItemList!![position]

        val itemTitle = holder.itemTitle
        // TextView itemDate = holder.itemDate;
        val itemContent = holder.itemContent
        // ChipGroup itemProjects = holder.itemProjects;
        // ChipGroup itemTags = holder.itemTags;
        if (!TextUtils.isEmpty(item.title)) {
            itemTitle.text = item.title
        } else {
            itemTitle.setText(R.string.task_adapter_empty_title)
        }
        /*if (item.dueDate != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.ENGLISH);
            String date = dateFormat.format(item.dueDate.toDate().getTime());
            itemDate.setText(date);
        }*/
        if (!TextUtils.isEmpty(item.content)) {
            Markwon.setMarkdown(itemContent, item.content!!)
        } else {
            itemContent.setText(R.string.task_adapter_empty_content)
        }
        /*if (checkNonEmpty(item.projects)) {
            for (String project : item.projects) {
                Chip tempChip = new Chip(mContext);
                tempChip.setText(project);
                itemProjects.addView(tempChip);
            }
        }
        if (checkNonEmpty(item.tags)) {
            for (String tag : item.tags) {
                Chip tempChip = new Chip(mContext);
                tempChip.setText(tag);
                itemTags.addView(tempChip);
            }
        }*/
        val markAsDoneBtn = holder.markAsDoneBtn
        markAsDoneBtn.setText(R.string.action_mark_as_done)
        markAsDoneBtn.setOnClickListener {
            if (mItemListener != null) {
                if (mUseDeprecatedListener!!) {
                    updateDoneStatus(item, markAsDoneBtn, position)
                } else {
                    mItemListener!!.onMarkAsDoneButtonClick(item, position)
                }
            } else {
                if (!mUseDeprecatedListener!!) {
                    Log.w(TAG, "Please supply a listener for the mark as done button!")
                } else {
                    updateDoneStatus(item, markAsDoneBtn, position)
                }
            }
        }
        val deleteBtn = holder.deleteBtn
        deleteBtn.setOnClickListener {
            if (mItemListener != null) {
                if (mUseDeprecatedListener!!) {
                    deleteTask(position)
                } else {
                    mItemListener!!.onDeleteButtonClick(item, position)
                }
            } else {
                if (!mUseDeprecatedListener!!) {
                    Log.w(TAG, "Please supply a listener for the delete button!")
                } else {
                    if (mContext != null) {
                        val builder = MaterialAlertDialogBuilder(mContext!!)
                        builder.setTitle("Delete todo?")
                        builder.setPositiveButton(R.string.dialog_action_ok) { dialogInterface, _ ->
                            dialogInterface.dismiss()
                            deleteTask(item.id, position)
                        }
                        builder.setNegativeButton(R.string.dialog_action_cancel) { dialogInterface, _ -> dialogInterface.dismiss() }
                        builder.show()
                    } else {
                        Log.w(TAG, "Please supply a context for the dialog!")
                    }
                }
            }
        }
        if (mUseDeprecatedListener!!) {
            setAnimation(holder.itemView, position)
        }
        // TODO: Remove the following line once selection is fixed
        holder.itemView.setOnClickListener {
            mItemListener?.onItemClick(mTaskItemList!![position], position)
        }
    }

    /**
     * Deletes a task at a specific position
     *
     * @param position The adapter position
     */
    @Deprecated("This should ideally be left up to the consumer, instead of handling it in the adapter")
    fun deleteTask(position: Int) {
        deleteTask(mTaskItemList!![position].id, position)
    }

    /**
     * Deletes a task at a specific position
     *
     * @param taskId   The document ID
     * @param position The adapter position
     */
    @Deprecated("This should ideally be left up to the consumer, instead of handling it in the adapter")
    fun deleteTask(taskId: String?, position: Int) {
        val item = mTaskItemList!![position]
        SharedUtils.removeTask(taskId!!, mUser!!, mFirestore!!).addOnSuccessListener {
            notifyItemRemoved(position)
            mSnackbarView?.let {
                Snackbar.make(it, "Todo was deleted", Snackbar.LENGTH_LONG)
                    .setAction("Undo") {
                        SharedUtils.addTask(item, mUser!!, mFirestore!!).addOnFailureListener { e ->
                            Snackbar.make(
                                mSnackbarView!!,
                                "Couldn't restore todo: " + e.message,
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }.show()
            }
        }
    }

    override fun getItemCount(): Int {
        return mTaskItemList!!.size
    }

    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        internal var itemTitle: TextView = view.findViewById(R.id.itemTitle)
        // TextView itemDate;
        internal var itemContent: TextView
        // ChipGroup itemProjects;
        // ChipGroup itemTags;
        internal var markAsDoneBtn: Button = view.findViewById(R.id.itemMarkAsDone)
        internal var deleteBtn: Button = view.findViewById(R.id.itemDelete)
        internal var cardView: MaterialCardView = view.findViewById(R.id.itemCardView)

        init {
            // itemDate = view.findViewById(R.id.itemDate);
            itemContent = view.findViewById(R.id.itemContent)
            // itemProjects = view.findViewById(R.id.itemProjects);
            // itemTags = view.findViewById(R.id.itemTags);
        }

        fun getItemDetails(): TaskItemDetails {
            Log.d(TAG, "Adapter position: $adapterPosition")
            val id = mTaskItemList!![adapterPosition].id
            Log.d(TAG, "ID at adapter position $adapterPosition: $id")
            return TaskItemDetails(adapterPosition, id)
        }
    }

    interface OnItemClickListener {
        /**
         * Called when the item view is clicked on
         *
         * @param item     The task item at this position
         * @param position The position of the adapter
         */
        @Deprecated("Use {@link androidx.recyclerview.selection.SelectionTracker.Builder#withOnItemActivatedListener(OnItemActivatedListener)}")
        fun onItemClick(item: TaskItem, position: Int)

        /**
         * Called when the delete button is clicked on
         *
         * @param item     The task item at this position
         * @param position The position of the adapter
         */
        fun onDeleteButtonClick(item: TaskItem, position: Int)

        /**
         * Called when the mark as done button is clicked on
         *
         * @param item     The task item at this position
         * @param position The position of the adapter
         */
        fun onMarkAsDoneButtonClick(item: TaskItem, position: Int)
    }

    /**
     * Sets the on click listener for views in the holder
     *
     * @param listener The on click listener
     */
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mItemListener = listener
    }
}
