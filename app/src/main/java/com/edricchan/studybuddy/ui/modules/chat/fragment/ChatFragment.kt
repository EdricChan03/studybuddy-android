package com.edricchan.studybuddy.ui.modules.chat.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.Fragment
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.extensions.TAG
import com.edricchan.studybuddy.extensions.firebase.auth.getUserDocument
import com.edricchan.studybuddy.extensions.startActivity
import com.edricchan.studybuddy.interfaces.chat.Chat
import com.edricchan.studybuddy.ui.modules.chat.NewChatActivity
import com.edricchan.studybuddy.ui.modules.chat.adapter.ChatItemDetailsLookup
import com.edricchan.studybuddy.ui.modules.chat.adapter.ChatItemKeyProvider
import com.edricchan.studybuddy.ui.modules.chat.adapter.ChatsAdapter
import com.edricchan.studybuddy.ui.modules.chat.utils.ChatUtils
import com.edricchan.studybuddy.utils.UiUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class ChatFragment : Fragment(R.layout.frag_chat) {
    private var firestoreListener: ListenerRegistration? = null
    private var currentUser: FirebaseUser? = null
    private var selectionTracker: SelectionTracker<String>? = null
    private lateinit var fragmentView: View
    private lateinit var parentActivity: AppCompatActivity
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var chatUtils: ChatUtils

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            selectionTracker?.onRestoreInstanceState(savedInstanceState)
        }

        fragmentView = view

        UiUtils(parentActivity).bottomAppBarFab?.setOnClickListener {
            startActivity<NewChatActivity>()
        }

        val recyclerView = fragmentView.findViewById<RecyclerView>(R.id.chatListRecyclerView)
        firestoreListener = chatUtils.chatCollectionJoinedQuery
            .addSnapshotListener { docSnapshot, e ->
                if (e != null) {
                    Log.e(TAG, "An error occurred while retrieving the chats:", e)
                } else {
                    if (docSnapshot != null && !docSnapshot.isEmpty) {
                        val chatList = docSnapshot.toObjects<Chat>()

                        val adapter =
                            ChatsAdapter(requireContext(), chatList, onItemClickListener = { chat ->
                                Log.d(TAG, "Chat ${chat.id} clicked!")
                                Toast.makeText(
                                    context,
                                    "Chat ${chat.name} clicked!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }, onItemLongClickListener = { chat ->
                                Log.d(TAG, "Chat ${chat.id} long clicked!")
                                Toast.makeText(
                                    context,
                                    "Chat ${chat.name} long clicked!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                true
                            })
                        recyclerView.adapter = adapter
                        recyclerView.setHasFixedSize(false)
                        recyclerView.layoutManager = LinearLayoutManager(requireContext())

                        selectionTracker = SelectionTracker.Builder(
                            "chat-selection",
                            recyclerView,
                            ChatItemKeyProvider(chatList),
                            ChatItemDetailsLookup(recyclerView),
                            StorageStrategy.createStringStorage()
                        )
                            .withSelectionPredicate(SelectionPredicates.createSelectAnything())
                            .build()
                        adapter.selectionTracker = selectionTracker
                        selectionTracker?.addObserver(object :
                            SelectionTracker.SelectionObserver<String>() {
                            override fun onSelectionChanged() {
                                super.onSelectionChanged()
                                var mode: ActionMode? = null
                                if (mode == null) {
                                    mode = parentActivity.startSupportActionMode(object :
                                        ActionMode.Callback {
                                        override fun onActionItemClicked(
                                            mode: ActionMode?,
                                            item: MenuItem?
                                        ): Boolean {
                                            var isCallbackHandled = false
                                            var isActionModeFinished = false

                                            val selectedChatsWithNoAdminPerm = mutableListOf<Chat>()
                                            val selectedChatsWithAdminPerm = mutableListOf<Chat>()
                                            val selectedChatsIsOwner = mutableListOf<Chat>()

                                            selectionTracker?.selection?.forEach { chatId ->
                                                val chat = chatList.find { it.id == chatId }
                                                if (chat != null) {
                                                    if (chat.owner == currentUser?.getUserDocument(
                                                            firestore
                                                        ) &&
                                                        chat.admins?.contains(
                                                            currentUser?.getUserDocument(
                                                                firestore
                                                            )
                                                        ) == true
                                                    ) {
                                                        Log.d(
                                                            TAG,
                                                            "Current user is an owner of the chat ${chat.id}."
                                                        )
                                                        selectedChatsIsOwner.add(chat)
                                                        selectedChatsWithAdminPerm.add(chat)
                                                    } else if (chat.admins?.contains(
                                                            currentUser?.getUserDocument(
                                                                firestore
                                                            )
                                                        ) == true
                                                    ) {
                                                        Log.d(
                                                            TAG,
                                                            "Current user is an admin of the chat ${chat.id}."
                                                        )
                                                        selectedChatsWithAdminPerm.add(chat)
                                                    } else {
                                                        Log.d(
                                                            TAG,
                                                            "Current user is not an admin of the chat ${chat.id}."
                                                        )
                                                        selectedChatsWithNoAdminPerm.add(chat)
                                                    }
                                                } else {
                                                    Log.w(
                                                        TAG,
                                                        "Chat $chatId does not exist. Skipping..."
                                                    )
                                                }
                                            }

                                            when (item?.itemId) {
                                                R.id.cab_action_archive_selected_chats -> {
                                                    if (selectedChatsWithNoAdminPerm.size == 0) {
                                                        MaterialAlertDialogBuilder(requireContext()).apply {
                                                            setTitle(R.string.chat_frag_archive_selected_chats_confirm_dialog_title)
                                                            setMessage(R.string.chat_frag_archive_selected_chats_confirm_dialog_msg)
                                                            setPositiveButton(R.string.chat_frag_archive_selected_chats_confirm_dialog_positive_btn_text) { dialog,
                                                                                                                                                            _ ->
                                                                val selectedChats =
                                                                    selectedChatsIsOwner + selectedChatsWithAdminPerm

                                                                firestore.runBatch { batch ->
                                                                    selectedChats.forEach { chat ->
                                                                        batch.update(
                                                                            firestore.document("chats/${chat.id}"),
                                                                            mapOf(
                                                                                "archived" to true
                                                                            )
                                                                        )
                                                                    }
                                                                }.addOnCompleteListener { task ->
                                                                    if (task.isSuccessful) {
                                                                        Log.d(
                                                                            TAG,
                                                                            "Successfully archived selected chats (${selectedChats.joinToString { it.id.toString() }})!"
                                                                        )
                                                                        Toast.makeText(
                                                                            context,
                                                                            "Successfully archived selected chats!",
                                                                            Toast.LENGTH_SHORT
                                                                        ).show()
                                                                        dialog.dismiss()
                                                                        isActionModeFinished = true
                                                                    } else {
                                                                        Log.e(
                                                                            TAG,
                                                                            "An error occurred while attempting to archive selected chats:",
                                                                            task.exception
                                                                        )
                                                                        Toast.makeText(
                                                                            context,
                                                                            "An error occurred while attempting to archive the selected chats. Try " +
                                                                                    "again later.",
                                                                            Toast.LENGTH_LONG
                                                                        ).show()
                                                                    }
                                                                }
                                                            }
                                                            setNegativeButton(R.string.chat_frag_archive_selected_chats_confirm_dialog_negative_btn_text) { dialog,
                                                                                                                                                            _ ->
                                                                dialog.dismiss()
                                                            }
                                                        }.show()
                                                    } else {
                                                        val reason =
                                                            getString(R.string.chat_frag_archive_selected_chats_denied_dialog_reason_not_admin,
                                                                "- " +
                                                                        selectedChatsWithNoAdminPerm.joinToString(
                                                                            "\n- "
                                                                        ) { it.name.toString() })
                                                        MaterialAlertDialogBuilder(requireContext()).apply {
                                                            setTitle(R.string.chat_frag_archive_selected_chats_denied_dialog_title)
                                                            setMessage(
                                                                getString(
                                                                    R.string.chat_frag_archive_selected_chats_denied_dialog_msg,
                                                                    reason
                                                                )
                                                            )
                                                            setPositiveButton(R.string.chat_frag_archive_selected_chats_denied_dialog_positive_btn_text) { dialog, _ -> dialog.dismiss() }
                                                        }.show()
                                                        isActionModeFinished = true
                                                    }
                                                    isCallbackHandled = true
                                                }
                                                R.id.cab_action_leave_selected_chats -> {
                                                    if (selectedChatsIsOwner.size == 0) {
                                                        MaterialAlertDialogBuilder(requireContext()).apply {
                                                            setTitle(R.string.chat_frag_leave_selected_chats_confirm_dialog_title)
                                                            if (selectedChatsWithAdminPerm.size > 0) {
                                                                setMessage(R.string.chat_frag_leave_selected_chats_confirm_dialog_msg_with_admin)
                                                            } else {
                                                                setMessage(R.string.chat_frag_leave_selected_chats_confirm_dialog_msg)
                                                            }
                                                            setPositiveButton(R.string.chat_frag_leave_selected_chats_confirm_dialog_positive_btn_text) { dialog, _ ->
                                                                firestore.runBatch { batch ->
                                                                    selectedChatsWithAdminPerm.forEach { chat ->
                                                                        batch.update(
                                                                            firestore.document("chats/${chat.id}"),
                                                                            mapOf(
                                                                                "admins" to FieldValue.arrayRemove(
                                                                                    currentUser?.getUserDocument(
                                                                                        firestore
                                                                                    )
                                                                                ),
                                                                                "members" to FieldValue.arrayRemove(
                                                                                    currentUser?.getUserDocument(
                                                                                        firestore
                                                                                    )
                                                                                )
                                                                            )
                                                                        )
                                                                    }
                                                                    selectedChatsWithNoAdminPerm.forEach { chat ->
                                                                        batch.update(
                                                                            firestore.document("chats/${chat.id}"),
                                                                            mapOf(
                                                                                "members" to FieldValue.arrayRemove(
                                                                                    currentUser?.getUserDocument(
                                                                                        firestore
                                                                                    )
                                                                                )
                                                                            )
                                                                        )
                                                                    }
                                                                }.addOnCompleteListener { task ->
                                                                    val selectedChats =
                                                                        selectedChatsWithAdminPerm + selectedChatsWithNoAdminPerm
                                                                    if (task.isSuccessful) {
                                                                        Log.d(
                                                                            TAG,
                                                                            "Successfully left selected chats (${selectedChats.joinToString { it.id.toString() }})!"
                                                                        )
                                                                        Toast.makeText(
                                                                            context,
                                                                            "Successfully left selected chats!",
                                                                            Toast.LENGTH_SHORT
                                                                        ).show()
                                                                        dialog.dismiss()
                                                                        isActionModeFinished = true
                                                                    } else {
                                                                        Log.e(
                                                                            TAG,
                                                                            "An error occurred while attempting to leave selected chats (" +
                                                                                    "${selectedChats.joinToString { it.id.toString() }}):",
                                                                            task.exception
                                                                        )
                                                                        Toast.makeText(
                                                                            context,
                                                                            "An error occurred while attempting to leave selected chats. Try again " +
                                                                                    "later.",
                                                                            Toast.LENGTH_LONG
                                                                        ).show()
                                                                    }
                                                                }
                                                            }
                                                            setNegativeButton(R.string.chat_frag_leave_selected_chats_confirm_dialog_negative_btn_text) { dialog, _
                                                                ->
                                                                dialog.dismiss()
                                                            }
                                                        }.show()
                                                    } else {
                                                        val reason =
                                                            getString(R.string.chat_frag_leave_selected_chats_denied_reason_is_owner,
                                                                "-" +
                                                                        selectedChatsIsOwner.joinToString(
                                                                            "\n- "
                                                                        ) { it.name.toString() })
                                                        MaterialAlertDialogBuilder(requireContext()).apply {
                                                            setTitle(R.string.chat_frag_leave_selected_chats_denied_dialog_title)
                                                            setMessage(
                                                                getString(
                                                                    R.string.chat_frag_leave_selected_chats_denied_dialog_msg,
                                                                    reason
                                                                )
                                                            )
                                                            setPositiveButton(R.string.chat_frag_leave_selected_chats_denied_dialog_positive_btn_text) { dialog, _
                                                                ->
                                                                dialog.dismiss()
                                                            }
                                                        }.show()
                                                        isActionModeFinished = true
                                                    }

                                                    isCallbackHandled = true
                                                }
                                                R.id.cab_action_delete_selected_chats -> {
                                                    MaterialAlertDialogBuilder(requireContext()).apply {
                                                        setTitle(R.string.chat_frag_delete_selected_chats_confirm_dialog_title)
                                                        setMessage(
                                                            getString(R.string.chat_frag_delete_selected_chats_confirm_dialog_msg,
                                                                "-" +
                                                                        selectedChatsWithAdminPerm.joinToString(
                                                                            "\n- "
                                                                        ) { it.name.toString() })
                                                        )
                                                        setPositiveButton(R.string.chat_frag_delete_selected_chats_confirm_dialog_positive_btn_text) { dialog, _
                                                            ->
                                                            val deleteBatch = firestore.batch()

                                                            selectedChatsWithAdminPerm.forEach { chat ->
                                                                deleteBatch.delete(
                                                                    firestore.document(
                                                                        "chats/${chat.id}"
                                                                    )
                                                                )
                                                            }
                                                            deleteBatch.commit()
                                                                .addOnCompleteListener { task ->
                                                                    if (task.isSuccessful) {
                                                                        Log.d(
                                                                            TAG,
                                                                            "Successfully deleted selected chats (" +
                                                                                    "${selectedChatsWithAdminPerm.joinToString { it.id.toString() }})!"
                                                                        )
                                                                        Toast.makeText(
                                                                            context,
                                                                            "Successfully deleted selected chats!",
                                                                            Toast.LENGTH_SHORT
                                                                        ).show()
                                                                    } else {
                                                                        Log.e(
                                                                            TAG,
                                                                            "An error occurred while attempting to delete the selected chats (" +
                                                                                    "${selectedChatsWithAdminPerm.joinToString { it.id.toString() }}):",
                                                                            task.exception
                                                                        )
                                                                        Toast.makeText(
                                                                            context,
                                                                            "An error occurred while attempting to delete the selected chats. Try again " +
                                                                                    "later.",
                                                                            Toast.LENGTH_LONG
                                                                        ).show()
                                                                    }
                                                                }
                                                            dialog.dismiss()
                                                        }
                                                        setNegativeButton(R.string.chat_frag_delete_selected_chats_confirm_dialog_negative_btn_text) { dialog, _
                                                            ->
                                                            dialog.dismiss()
                                                        }
                                                    }.show()
                                                    isCallbackHandled = true
                                                }
                                                R.id.cab_action_select_all_chats -> {
                                                    selectionTracker?.setItemsSelected(
                                                        chatList.map { it.id },
                                                        true
                                                    )

                                                    isCallbackHandled = true
                                                    isActionModeFinished = true
                                                }
                                                else -> isCallbackHandled = false
                                            }
                                            if (isActionModeFinished) {
                                                // Close the action mode
                                                mode?.finish()
                                            }
                                            return isCallbackHandled
                                        }

                                        override fun onCreateActionMode(
                                            mode: ActionMode?,
                                            menu: Menu?
                                        ): Boolean {
                                            // Clear any existing menu items
                                            menu?.clear()

                                            val inflater = mode?.menuInflater
                                            inflater?.inflate(R.menu.cab_chat, menu)
                                            return true
                                        }

                                        override fun onDestroyActionMode(mode: ActionMode?) {
                                            selectionTracker?.clearSelection()
                                        }

                                        override fun onPrepareActionMode(
                                            mode: ActionMode?,
                                            menu: Menu?
                                        ): Boolean {
                                            var returnValue = false
                                            if (selectionTracker != null && selectionTracker?.hasSelection() == true) {
                                                if (selectionTracker?.selection?.size() == chatList.size) {
                                                    menu?.removeItem(R.id.cab_action_select_all_chats)
                                                    returnValue = true
                                                }
                                            }
                                            val hasDisabled = run {
                                                selectionTracker?.selection?.forEach { chatId ->
                                                    val chat = chatList.find { it.id == chatId }
                                                    var hasMenuItemDisabled = false
                                                    if (chat != null) {
                                                        if (chat.owner == currentUser?.getUserDocument(
                                                                firestore
                                                            )
                                                        ) {
                                                            // Disable the leave, delete and archive selected chats menu item
                                                            menu?.findItem(R.id.cab_action_leave_selected_chats)
                                                                ?.isEnabled = false
                                                            menu?.findItem(R.id.cab_action_delete_selected_chats)
                                                                ?.isEnabled = false
                                                            menu?.findItem(R.id.cab_action_archive_selected_chats)
                                                                ?.isEnabled = false
                                                            hasMenuItemDisabled = true
                                                        }
                                                        if (chat.admins?.contains(
                                                                currentUser?.getUserDocument(
                                                                    firestore
                                                                )
                                                            ) == false
                                                        ) {
                                                            // Disable the delete and archive selected chats menu item
                                                            menu?.findItem(R.id.cab_action_delete_selected_chats)
                                                                ?.isEnabled = false
                                                            menu?.findItem(R.id.cab_action_archive_selected_chats)
                                                                ?.isEnabled = false
                                                            returnValue = true
                                                            hasMenuItemDisabled = true
                                                        }
                                                        if (hasMenuItemDisabled) return@run true
                                                    }
                                                }
                                                return@run false
                                            }

                                            if (!hasDisabled) {
                                                if (menu?.findItem(R.id.cab_action_leave_selected_chats)?.isEnabled == false) {
                                                    menu.findItem(R.id.cab_action_leave_selected_chats)
                                                        ?.isEnabled = true
                                                }
                                                if (menu?.findItem(R.id.cab_action_archive_selected_chats)?.isEnabled == false) {
                                                    menu.findItem(R.id.cab_action_archive_selected_chats)
                                                        ?.isEnabled = true
                                                }
                                                if (menu?.findItem(R.id.cab_action_delete_selected_chats)?.isEnabled == false) {
                                                    menu.findItem(R.id.cab_action_delete_selected_chats)
                                                        ?.isEnabled = true
                                                }
                                            }

                                            return returnValue
                                        }
                                    })
                                    mode?.setTitle(R.string.cab_title)
                                } else if (selectionTracker != null && selectionTracker?.hasSelection() == true) {
                                    mode.invalidate()
                                    mode.subtitle = selectionTracker?.selection?.size()?.let {
                                        context?.resources?.getQuantityString(
                                            R.plurals.cab_subtitle,
                                            /* quantity */ it, /* formatArgs */ it
                                        )
                                    }
                                }

                                Log.d(TAG, "Current selection: ${selectionTracker?.selection}")
                            }
                        })
                    }
                }
            }

    }

    override fun onAttach(context: Context) {
        parentActivity = context as AppCompatActivity
        super.onAttach(context as Context)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (selectionTracker != null) {
            selectionTracker?.onSaveInstanceState(outState)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = Firebase.firestore
        auth = Firebase.auth
        currentUser = auth.currentUser
        chatUtils = ChatUtils.getInstance(auth, firestore)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (firestoreListener != null) {
            firestoreListener?.remove()
        }
    }
}
