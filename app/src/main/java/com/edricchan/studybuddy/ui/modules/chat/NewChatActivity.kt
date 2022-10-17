package com.edricchan.studybuddy.ui.modules.chat

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.core.deeplink.AppDeepLink
import com.edricchan.studybuddy.core.deeplink.WebDeepLink
import com.edricchan.studybuddy.extensions.TAG
import com.edricchan.studybuddy.extensions.editTextStrValue
import com.edricchan.studybuddy.extensions.firebase.auth.getUserDocument
import com.edricchan.studybuddy.extensions.startActivity
import com.edricchan.studybuddy.extensions.startActivityForResult
import com.edricchan.studybuddy.interfaces.Visibility
import com.edricchan.studybuddy.interfaces.chat.Chat
import com.edricchan.studybuddy.ui.common.BaseActivity
import com.edricchan.studybuddy.ui.modules.auth.LoginActivity
import com.edricchan.studybuddy.ui.widget.iconpicker.IconPickerActivity
import com.esafirm.imagepicker.features.ImagePickerConfig
import com.esafirm.imagepicker.features.ImagePickerMode
import com.esafirm.imagepicker.features.registerImagePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@WebDeepLink(["/chats/new"])
@AppDeepLink(["/chats/new"])
class NewChatActivity : BaseActivity(R.layout.activity_new_chat) {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private val ICON_PICKER_RESULT = 100

    private val imagePickerLauncher = registerImagePicker {
        // TODO: Add proper image picker support
        Log.d(TAG, "Selected images: $it")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        auth = Firebase.auth
        firestore = Firebase.firestore

        if (auth.currentUser == null) {
            Toast.makeText(this, "Please sign in before continuing", Toast.LENGTH_SHORT).show()
            finish()
            startActivity<LoginActivity>()
        }

        @SuppressLint("DefaultLocale")
        val visibilityValues =
            listOf(Visibility.PUBLIC, Visibility.UNLISTED, Visibility.PRIVATE).map {
                it.capitalize()
            }

        findViewById<AutoCompleteTextView>(R.id.chatVisibilityAutoCompleteTextView).apply {
            setAdapter(
                ArrayAdapter(
                    context,
                    R.layout.dropdown_menu_popup_item,
                    visibilityValues
                )
            )
        }

        val chatIconImageButton = findViewById<FloatingActionButton>(R.id.imageButtonChatIconPicker)
        chatIconImageButton.setOnClickListener {
            // TODO: Show a dialog with options to choose from library or upload
            MaterialAlertDialogBuilder(this@NewChatActivity).apply {
                setTitle(R.string.new_chat_activity_select_chat_icon_dialog_title)
                setItems(R.array.new_chat_activity_select_chat_icon_dialog_items) { dialog, i ->
                    when (i) {
                        0 -> {
                            // Choose from library
                            // TODO: Add activity for this functionality
                            startActivityForResult<IconPickerActivity>(ICON_PICKER_RESULT)
                        }

                        1 -> {
                            // Upload
                            imagePickerLauncher.launch(ImagePickerConfig {
                                theme = R.style.Theme_App_ImagePicker
                                mode = ImagePickerMode.SINGLE
                                isFolderMode = true
                            })
                        }

                        2 -> {
                            // Default
                            // TODO: Add functionality
                        }
                    }
                }
                setNegativeButton(R.string.dialog_action_cancel, null)
            }.show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_new_item, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            R.id.action_submit -> {
                // NOTE: TIL means TextInputLayout here
                val chatNameTIL = findViewById<TextInputLayout>(R.id.chatNameTextInputLayout)
                val chatDescTIL = findViewById<TextInputLayout>(R.id.chatDescTextInputLayout)
                val chatVisibilityAutoCompleteTextView =
                    findViewById<AutoCompleteTextView>(R.id.chatVisibilityAutoCompleteTextView)

                val chatName = chatNameTIL.editTextStrValue
                val chatDescription = chatDescTIL.editTextStrValue

                @SuppressLint("DefaultLocale")
                val chatVisibility =
                    chatVisibilityAutoCompleteTextView.text.toString().decapitalize()

                if (chatName.isEmpty() || chatName.length > 100 || chatDescription.length > 300) {
                    if ((chatName.isEmpty() || chatName.length > 100) && !chatNameTIL.isErrorEnabled) chatNameTIL.isErrorEnabled =
                        true
                    if (chatName.isEmpty()) {
                        chatNameTIL.error = "Please specify a chat name!"
                    } else if (chatName.length > 100) {
                        chatNameTIL.error = "Only 100 characters are allowed."
                    }

                    if ((chatDescription.length > 300) && !chatDescTIL.isErrorEnabled) chatDescTIL.isErrorEnabled =
                        true
                    if (chatDescription.length > 300) {
                        chatDescTIL.error = "Only 300 characters are allowed."
                    }

                    Snackbar.make(
                        findViewById(R.id.mainView),
                        "Some errors occurred while attempting to submit the form.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    if (chatNameTIL.isErrorEnabled) {
                        // Disable the error
                        chatNameTIL.isErrorEnabled = false
                        chatNameTIL.error = null
                    }
                    if (chatDescTIL.isErrorEnabled) {
                        // Disable the error
                        chatDescTIL.isErrorEnabled = false
                        chatDescTIL.error = null
                    }

                    val chat = Chat.build {
                        name = chatName
                        description = chatDescription
                        visibility = chatVisibility
                        owner = auth.currentUser?.getUserDocument(firestore)
                        admins = mutableListOf(auth.currentUser?.getUserDocument(firestore))
                        members = mutableListOf(auth.currentUser?.getUserDocument(firestore))
                    }

                    firestore.collection("chats").add(chat).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "Successfully created chat at ${task.result?.id}!")
                            Toast.makeText(this, "Successfully created chat!", Toast.LENGTH_SHORT)
                                .show()
                            finish()
                        } else {
                            Log.e(
                                TAG,
                                "An error occurred while attempting to create the chat:",
                                task.exception
                            )
                            Toast.makeText(
                                this,
                                "An error occurred while attempting to create the chat. Try again later.",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                    }
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ICON_PICKER_RESULT) {
            Log.d(TAG, "Icon picker returned!")
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
