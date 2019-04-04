package com.edricchan.studybuddy.ui.modules.chat

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.ui.modules.auth.LoginActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_view_task.*

class NewChatActivity : AppCompatActivity(R.layout.activity_new_chat) {

	private lateinit var mAuth: FirebaseAuth

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		mAuth = FirebaseAuth.getInstance()

		if (mAuth.currentUser == null) {
			Toast.makeText(this, "Please sign in before continuing", Toast.LENGTH_SHORT).show()
			val signInIntent = Intent(this, LoginActivity::class.java)
			startActivity(signInIntent)
		} else {
		}
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.menu_new_task, menu)
		return super.onCreateOptionsMenu(menu)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		return when (item.itemId) {
			android.R.id.home -> {
				onBackPressed()
				true
			}
			R.id.action_submit -> {
				Snackbar.make(mainView, "TODO", Snackbar.LENGTH_SHORT)
						.show()
				true
			}
			else -> super.onOptionsItemSelected(item)
		}
	}


}