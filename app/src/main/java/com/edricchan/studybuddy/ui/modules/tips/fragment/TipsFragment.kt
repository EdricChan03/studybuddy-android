package com.edricchan.studybuddy.ui.modules.tips.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.databinding.FragTipsBinding
import com.edricchan.studybuddy.extensions.startActivity
import com.edricchan.studybuddy.ui.modules.debug.DebugActivity
import com.edricchan.studybuddy.ui.modules.settings.SettingsActivity
import com.edricchan.studybuddy.utils.defaultSharedPreferences
import com.edricchan.studybuddy.utils.launchUri

class TipsFragment : Fragment() {
    private var preferences: SharedPreferences? = null
    private var _binding: FragTipsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        preferences = requireContext().defaultSharedPreferences
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tipsEmptyStateCta.setOnClickListener {
            requireContext().launchUri(Constants.uriSubmitTip)
        }
    }


    override fun onPrepareOptionsMenu(menu: Menu) {
        if (!BuildConfig.DEBUG) {
            menu.removeItem(R.id.action_debug)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Clear the main activity's menu before inflating the fragment's menu
        menu.clear()
        inflater.inflate(R.menu.menu_frag_tips, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            /*R.id.action_submit_tip -> {
                WebUtils.getInstance(requireContext).launchUri(Constants.uriSubmitTip)
                true
            }*/
            R.id.action_debug -> {
                startActivity<DebugActivity>()
                true
            }

            R.id.action_settings -> {
                startActivity<SettingsActivity>()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
