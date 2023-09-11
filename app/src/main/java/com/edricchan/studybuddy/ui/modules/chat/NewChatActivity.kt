package com.edricchan.studybuddy.ui.modules.chat

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.core.deeplink.AppDeepLink
import com.edricchan.studybuddy.core.deeplink.WebDeepLink
import com.edricchan.studybuddy.databinding.ActivityNewChatBinding
import com.edricchan.studybuddy.extensions.TAG
import com.edricchan.studybuddy.extensions.dialog.showMaterialAlertDialog
import com.edricchan.studybuddy.extensions.editTextStrValue
import com.edricchan.studybuddy.extensions.showSnackbar
import com.edricchan.studybuddy.exts.android.startActivity
import com.edricchan.studybuddy.exts.firebase.auth.getUserDocument
import com.edricchan.studybuddy.interfaces.Visibility
import com.edricchan.studybuddy.interfaces.chat.Chat
import com.edricchan.studybuddy.ui.common.BaseActivity
import com.edricchan.studybuddy.ui.modules.auth.LoginActivity
import com.edricchan.studybuddy.ui.widget.iconpicker.IconPickerActivity
import com.esafirm.imagepicker.features.ImagePickerConfig
import com.esafirm.imagepicker.features.ImagePickerMode
import com.esafirm.imagepicker.features.registerImagePicker
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Locale
import com.edricchan.studybuddy.ui.theming.R as ThemingR

@WebDeepLink(["/chats/new"])
@AppDeepLink(["/chats/new"])
class NewChatActivity : BaseActivity() {
    private lateinit var binding: ActivityNewChatBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    companion object {
        private val ICON_PICKER_RESULT = 100
    }

    private val imagePickerLauncher = registerImagePicker {
        // TODO: Add proper image picker support
        Log.d(TAG, "Selected images: $it")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding = ActivityNewChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        firestore = Firebase.firestore

        if (auth.currentUser == null) {
            Toast.makeText(this, "Please sign in before continuing", Toast.LENGTH_SHORT).show()
            finish()
            startActivity<LoginActivity>()
        }

        @SuppressLint("DefaultLocale")
        val visibilityValues = Visibility.values.map { value ->
            value.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            }
        }

        binding.apply {
            chatVisibilityAutoCompleteTextView.setAdapter(
                ArrayAdapter(
                    this@NewChatActivity,
                    R.layout.dropdown_menu_popup_item,
                    visibilityValues
                )
            )

            imageButtonChatIconPicker.setOnClickListener {
                // TODO: Show a dialog with options to choose from library or upload
                showMaterialAlertDialog {
                    setTitle(R.string.new_chat_activity_select_chat_icon_dialog_title)
                    setItems(R.array.new_chat_activity_select_chat_icon_dialog_items) { _, i ->
                        when (i) {
                            0 -> {
                                // Choose from library
                                // TODO: Add activity for this functionality
                                @Suppress("DEPRECATION")
                                startActivityForResult(
                                    Intent(
                                        this@NewChatActivity,
                                        IconPickerActivity::class.java
                                    ), ICON_PICKER_RESULT
                                )
                            }

                            1 -> {
                                // Upload
                                imagePickerLauncher.launch(ImagePickerConfig {
                                    theme = ThemingR.style.Theme_App_ImagePicker
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
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_new_item, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }

            R.id.action_submit -> {
                // NOTE: TIL means TextInputLayout here
                val chatNameTIL = binding.chatNameTextInputLayout
                val chatDescTIL = binding.chatDescTextInputLayout
                val chatVisibilityAutoCompleteTextView =
                    binding.chatVisibilityAutoCompleteTextView

                val chatName = chatNameTIL.editTextStrValue
                val chatDescription = chatDescTIL.editTextStrValue

                @SuppressLint("DefaultLocale")
                val chatVisibility =
                    chatVisibilityAutoCompleteTextView.text.toString()
                        .replaceFirstChar { it.lowercase(Locale.getDefault()) }

                if (chatName.isEmpty() || chatName.length > 100 || chatDescription.length > 300) {
                    if (chatName.isEmpty()) {
                        chatNameTIL.error = "Please specify a chat name!"
                    } else if (chatName.length > 100) {
                        chatNameTIL.error = "Only 100 characters are allowed."
                    }

                    if (chatDescription.length > 300) {
                        chatDescTIL.error = "Only 300 characters are allowed."
                    }

                    showSnackbar(
                        binding.mainView,
                        "Some errors occurred while attempting to submit the form.",
                        Snackbar.LENGTH_SHORT
                    )
                } else {
                    chatNameTIL.error = null
                    chatDescTIL.error = null

                    val chat = Chat.build {
                        name = chatName
                        description = chatDescription
                        visibility = chatVisibility
                        owner = auth.currentUser?.getUserDocument(firestore)
                        admins?.add(auth.currentUser?.getUserDocument(firestore))
                        members?.add(auth.currentUser?.getUserDocument(firestore))
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

    // TODO: Migrate to Activity Result API
    @Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ICON_PICKER_RESULT) {
            Log.d(TAG, "Icon picker returned!")
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
