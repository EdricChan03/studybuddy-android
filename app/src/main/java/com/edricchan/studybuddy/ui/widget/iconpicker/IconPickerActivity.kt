package com.edricchan.studybuddy.ui.widget.iconpicker

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.databinding.ActivityIconPickerBinding
import com.edricchan.studybuddy.extensions.TAG
import com.edricchan.studybuddy.extensions.showToast
import com.edricchan.studybuddy.ui.common.BaseActivity
import com.edricchan.studybuddy.ui.widget.iconpicker.adapter.IconPickerAdapter
import com.edricchan.studybuddy.ui.widget.iconpicker.adapter.IconPickerItemDetailsLookup
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class IconPickerActivity : BaseActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var adapter: IconPickerAdapter
    private lateinit var recyclerView: RecyclerView

    private lateinit var binding: ActivityIconPickerBinding
    private var recyclerViewLayout: IconPickerAdapter.HolderLayout =
        IconPickerAdapter.HolderLayout.LIST
    private lateinit var tracker: SelectionTracker<Long>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (savedInstanceState != null) {
            tracker.onRestoreInstanceState(savedInstanceState)
        }
        firestore = Firebase.firestore

        binding = ActivityIconPickerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.recyclerView
        updateRecyclerViewLayout(IconPickerAdapter.HolderLayout.LIST)
        recyclerView.setHasFixedSize(false)

        adapter = IconPickerAdapter()
        recyclerView.adapter = adapter

        tracker = SelectionTracker.Builder(
            "icon-picker-selection",
            recyclerView,
            StableIdKeyProvider(recyclerView),
            IconPickerItemDetailsLookup(recyclerView),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(SelectionPredicates.createSelectSingleAnything())
            .withOnItemActivatedListener { item, _ ->
                showToast(
                    "Position ${item.position} has been activated.",
                    Toast.LENGTH_SHORT
                )
                true
            }
            .build()

        adapter.tracker = tracker
        adapter.infoButtonOnClickListener = { chatIcon ->
            IconPickerInfoBottomSheetFragment(icon = chatIcon).show(
                supportFragmentManager,
                "iconInfo"
            )
        }

        firestore.collection("chatIconLibrary")
            .addSnapshotListener { snapshot, exception ->
                if (snapshot != null && !snapshot.isEmpty && exception == null) {
                    adapter.items = snapshot.toObjects()
                    adapter.notifyDataSetChanged()
                } else {
                    Log.e(
                        TAG,
                        "An error occurred while attempting to retrieve the library:",
                        exception
                    )
                }
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        tracker.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_icon_picker, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_switch_list_view -> {
                updateRecyclerViewLayout(IconPickerAdapter.HolderLayout.LIST)
                invalidateOptionsMenu()
                true
            }

            R.id.action_switch_grid_view -> {
                updateRecyclerViewLayout(IconPickerAdapter.HolderLayout.GRID)
                invalidateOptionsMenu()
                true
            }

            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val isList = recyclerViewLayout == IconPickerAdapter.HolderLayout.LIST
        menu.findItem(R.id.action_switch_grid_view)?.isVisible = isList
        menu.findItem(R.id.action_switch_list_view)?.isVisible = !isList
        return true
    }

    private fun updateRecyclerViewLayout(layout: IconPickerAdapter.HolderLayout) {
        recyclerViewLayout = layout
        adapter.setHolderLayout(layout)
        if (layout == IconPickerAdapter.HolderLayout.LIST) {
            recyclerView.layoutManager = LinearLayoutManager(this)
        } else if (layout == IconPickerAdapter.HolderLayout.GRID) {
            recyclerView.layoutManager = GridLayoutManager(this, 3)
        }
    }
}
