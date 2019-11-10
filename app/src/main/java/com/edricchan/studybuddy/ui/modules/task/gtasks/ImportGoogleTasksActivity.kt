package com.edricchan.studybuddy.ui.modules.task.gtasks

import android.accounts.Account
import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.extensions.TAG
import com.edricchan.studybuddy.extensions.showToast
import com.edricchan.studybuddy.extensions.startActivity
import com.edricchan.studybuddy.ui.modules.auth.LoginActivity
import com.edricchan.studybuddy.ui.modules.task.gtasks.adapter.TaskListsAdapter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.Scope
import com.google.api.client.extensions.android.json.AndroidJsonFactory
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.tasks.Tasks
import com.google.api.services.tasks.TasksScopes
import com.google.api.services.tasks.model.TaskList
import java.lang.ref.WeakReference


class ImportGoogleTasksActivity : AppCompatActivity(R.layout.activity_import_google_tasks) {
	private lateinit var emptyStateLayout: LinearLayout
	private lateinit var emptyStateHeadingTextView: TextView
	private lateinit var emptyStateSubheadingTextView: TextView
	private lateinit var emptyStateCtaButton: Button
	private lateinit var recyclerView: RecyclerView
	//	private lateinit var taskApiUtils: GTasksApiUtils
	private val taskLists: MutableList<TaskList> = mutableListOf()
	private val adapter = TaskListsAdapter(this, taskLists, ::onAdapterClick)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)

//		initGTaskApi()
		initLayoutVars()
		initRecyclerView()
	}

	override fun onStart() {
		super.onStart()
		Log.d(TAG, "Current Google account display name: ${GoogleSignIn.getLastSignedInAccount(this)?.displayName}")
		Log.d(TAG, "Current Google account: ${GoogleSignIn.getLastSignedInAccount(this)?.account}")
		initEmptyStateLayout()
		addTaskListsToAdapter()
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (requestCode == RC_REQUEST_TASKS_READONLY_GRANT_SCOPE) {
			Log.d(TAG, "Grant activity successfully returned!")
			if (resultCode == Activity.RESULT_OK) {
				// Successfully granted permission!
				Log.d(TAG, "Successfully granted permission!")
				setEmptyStateLayoutOptions(View.GONE)
				addTaskListsToAdapter()
			}
		}
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		return when (item.itemId) {
			android.R.id.home -> {
				onBackPressed()
				true
			}
			else -> super.onOptionsItemSelected(item)
		}
	}

	private fun hasTaskReadonlyScope() = GoogleSignIn.hasPermissions(
			GoogleSignIn.getLastSignedInAccount(this), Scope(TasksScopes.TASKS_READONLY))

	/*private fun initGTaskApi(account: GoogleSignInAccount? = null) {
		val credential = GoogleAccountCredential.usingOAuth2(this,
				listOf(TasksScopes.TASKS_READONLY))
		if (account != null) {
			credential.selectedAccount = account.account
		} else {
			credential.selectedAccount = GoogleSignIn.getLastSignedInAccount(this)?.account
		}
		taskApiUtils = GTasksApiUtils(credential)
	}*/

	private fun initLayoutVars() {
		emptyStateLayout = findViewById(R.id.emptyStateLayout)
		emptyStateHeadingTextView = findViewById(R.id.emptyStateHeadingTextView)
		emptyStateSubheadingTextView = findViewById(R.id.emptyStateSubheadingTextView)
		emptyStateCtaButton = findViewById(R.id.emptyStateCtaButton)
		recyclerView = findViewById(R.id.recyclerView)
	}

	private fun initEmptyStateLayout() {
		if (GoogleSignIn.getLastSignedInAccount(this) == null) {
			// There's no currently signed-in user
			setEmptyStateLayoutOptions(View.VISIBLE,
					R.string.import_gtasks_activity_empty_state_heading_no_acct,
					R.string.import_gtasks_activity_empty_state_subheading_no_acct,
					R.string.import_gtasks_activity_empty_state_cta_no_acct,
					View.OnClickListener {
						startActivity<LoginActivity>()
					})
		} else {
			if (!hasTaskReadonlyScope()) {
				setEmptyStateLayoutOptions(View.VISIBLE,
						R.string.import_gtasks_activity_empty_state_heading_no_scopes,
						R.string.import_gtasks_activity_empty_state_subheading_no_scopes,
						R.string.import_gtasks_activity_empty_state_cta_no_scopes,
						View.OnClickListener {
							GoogleSignIn.requestPermissions(this, RC_REQUEST_TASKS_READONLY_GRANT_SCOPE,
									GoogleSignIn.getLastSignedInAccount(this),
									Scope(TasksScopes.TASKS_READONLY))
						})
			} else {
				// Good to go!
				setEmptyStateLayoutOptions(View.GONE)
			}
		}
	}

	private fun initRecyclerView() {
		recyclerView.apply {
			adapter = adapter
			layoutManager = LinearLayoutManager(this@ImportGoogleTasksActivity)
			setHasFixedSize(false)
		}

	}

	private fun addTaskListsToAdapter() {
		val account = GoogleSignIn.getLastSignedInAccount(this)
		Log.d(TAG, "addTaskListsToAdapter: Current instance of account: ${account?.account}")
		Log.d(TAG, "addTaskListsToAdapter: Current instance of account's display name: ${account?.displayName}")
		if (account != null && account.account != null && hasTaskReadonlyScope()) {
			try {
				val result = AsyncLoadTaskLists(this, account.account).execute().get()
				Log.d(TAG, "Result of items: ${result?.joinToString()}")
				if (result != null) {
					taskLists.addAll(result.toTypedArray())
					adapter.notifyDataSetChanged()
					// Toggle visibility of RecyclerView
					recyclerView.visibility = View.VISIBLE
					setEmptyStateLayoutOptions(View.GONE)
				} else {
					Log.w(TAG, "Could not add task lists to adapter.")
				}
			} catch (e: Exception) {
				Log.e(TAG, "Could not get task lists:", e)
			}

		} else {
			Log.w(TAG, "No signed-in account exists! Skipping...")
		}
	}

	private fun onAdapterClick(item: TaskList) {
		showToast("Item ${item.id} clicked!", Toast.LENGTH_SHORT)
	}

	private fun setEmptyStateLayoutOptions(visibility: Int) {
		emptyStateLayout.visibility = visibility
	}

	private fun setEmptyStateLayoutOptions(visibility: Int, @StringRes headingTextResId: Int,
	                                       @StringRes subheadingTextResId: Int,
	                                       @StringRes ctaButtonTextResId: Int,
	                                       buttonOnClickListener: View.OnClickListener) {
		emptyStateLayout.visibility = visibility
		emptyStateHeadingTextView.setText(headingTextResId)
		emptyStateSubheadingTextView.setText(subheadingTextResId)
		emptyStateCtaButton.apply {
			setText(ctaButtonTextResId)
			setOnClickListener(buttonOnClickListener)
		}
	}

	private fun setEmptyStateLayoutOptions(visibility: Int, headingText: CharSequence,
	                                       subheadingText: CharSequence,
	                                       ctaButtonText: CharSequence,
	                                       buttonOnClickListener: View.OnClickListener) {
		emptyStateLayout.visibility = visibility
		emptyStateHeadingTextView.text = headingText
		emptyStateSubheadingTextView.text = subheadingText
		emptyStateCtaButton.apply {
			text = ctaButtonText
			setOnClickListener(buttonOnClickListener)
		}
	}

	companion object {
		private const val RC_REQUEST_TASKS_READONLY_GRANT_SCOPE = 60
		private const val RC_REAUTHORIZE = 70

		private class AsyncLoadTaskLists(private val ref: ImportGoogleTasksActivity,
		                                 private val account: Account?) :
				AsyncTask<Void, Void, List<TaskList>?>() {

			private val activityRef: WeakReference<ImportGoogleTasksActivity> = WeakReference(ref)
			override fun doInBackground(vararg params: Void): List<TaskList>? {
				Log.d(TAG, "Passed in account: $account")
				var result: List<TaskList>? = null
				try {
					Log.d(TAG, "Hello world! This log should be printed before the credential set-up.")
					val credential =
							GoogleAccountCredential.usingOAuth2(
									activityRef.get(),
									listOf(TasksScopes.TASKS_READONLY)
							)
					credential.selectedAccount = account
					credential.backOff = ExponentialBackOff()
					Log.d(TAG, "All accounts: ${credential.allAccounts.joinToString()}")
					Log.d(TAG, "Selected account: ${credential.selectedAccount}")
					Log.d(TAG, "Selected account name: ${credential.selectedAccountName}")
					Log.d(TAG, "Scopes: ${credential.scope}")
					val service = Tasks.Builder(NetHttpTransport(), AndroidJsonFactory.getDefaultInstance(),
							credential)
							.setApplicationName("StudyBuddy/${BuildConfig.VERSION_NAME}")
							.build()
					Log.d(TAG, "Service root URL: ${service.rootUrl}")
					Log.d(TAG, "Service base URL: ${service.baseUrl}")
					Log.d(TAG, "Service path: ${service.servicePath}")
					Log.d(TAG, "Application name: ${service.applicationName}")
					Log.d(TAG, "Hello world! This log should be printed after the credential set-up.")
					val listTaskListsResponse = service
							.tasklists()
							.list()
							// Explicitly set max results
							.setMaxResults(20L)
							.execute()
					Log.d(TAG, "Result of request: ${listTaskListsResponse.toPrettyString()}")
					result = listTaskListsResponse.items
				} catch (userRecoverableException: UserRecoverableAuthIOException) {
					// Explain to the user again why you need these OAuth permissions
					// And prompt the resolution to the user again:
					Log.d(TAG, "User denied permissions! Exception:", userRecoverableException)
					activityRef.get()?.startActivityForResult(userRecoverableException.intent, RC_REAUTHORIZE)
				} catch (e: Exception) {
					// Other non-recoverable exceptions.
					Log.e(TAG, "An error occurred while attempting to grant permissions:", e)
				}

				Log.d(TAG, "Result: ${result.toString()}")

				return result
			}
		}
	}
}