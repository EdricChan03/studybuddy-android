package com.edricchan.studybuddy.ui.widget.iconpicker

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.extensions.TAG
import com.edricchan.studybuddy.ui.widget.iconpicker.adapter.IconPickerAdapter
import com.edricchan.studybuddy.ui.widget.iconpicker.adapter.IconPickerItemDetailsLookup
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects

class IconPickerActivity : AppCompatActivity(R.layout.activity_icon_picker) {
    lateinit var firestore: FirebaseFirestore
    var recyclerViewLayout: IconPickerAdapter.HolderLayout = IconPickerAdapter.HolderLayout.LIST
    lateinit var tracker: SelectionTracker<Long>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (savedInstanceState != null) {
            tracker.onRestoreInstanceState(savedInstanceState)
        }
        firestore = FirebaseFirestore.getInstance()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        if (recyclerViewLayout == IconPickerAdapter.HolderLayout.LIST) {
            recyclerView.layoutManager = LinearLayoutManager(this)
        } else if (recyclerViewLayout == IconPickerAdapter.HolderLayout.GRID) {
            recyclerView.layoutManager = GridLayoutManager(this, 3)
        }
        recyclerView.setHasFixedSize(false)

        val adapter = IconPickerAdapter(this)
        recyclerView.adapter = adapter

        tracker = SelectionTracker.Builder(
            "icon-picker-selection",
            recyclerView,
            StableIdKeyProvider(recyclerView),
            IconPickerItemDetailsLookup(recyclerView),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(SelectionPredicates.createSelectSingleAnything())
            .withOnItemActivatedListener { item, e ->
                Toast.makeText(
                    this@IconPickerActivity,
                    "Position ${item.position} has been activated.",
                    Toast.LENGTH_SHORT
                )
                    .show()
                true
            }
            .build()

        adapter.tracker = tracker
        adapter.infoButtonOnClickListener = { chatIcon ->
            val infoBottomSheetFragment = IconPickerInfoBottomSheetFragment()
            infoBottomSheetFragment.icon = chatIcon
            infoBottomSheetFragment.show(supportFragmentManager, "iconInfo")
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}