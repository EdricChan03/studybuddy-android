package com.edricchan.studybuddy.ui.modules.tips.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.databinding.FragTipsBinding
import com.edricchan.studybuddy.exts.androidx.fragment.startActivity
import com.edricchan.studybuddy.exts.androidx.preference.defaultSharedPreferences
import com.edricchan.studybuddy.ui.modules.debug.DebugActivity
import com.edricchan.studybuddy.ui.modules.settings.SettingsActivity
import com.edricchan.studybuddy.utils.web.launchUri

class TipsFragment : Fragment() {
    private var preferences: SharedPreferences? = null
    private var _binding: FragTipsBinding? = null
    private val binding get() = _binding!!

    private val menuProvider = object : MenuProvider {
        override fun onPrepareMenu(menu: Menu) {
            if (!BuildConfig.DEBUG) {
                menu.removeItem(R.id.action_debug)
            }
        }

        override fun onCreateMenu(menu: Menu, inflater: MenuInflater) {
            // Clear the main activity's menu before inflating the fragment's menu
            menu.clear()
            inflater.inflate(R.menu.menu_frag_tips, menu)
        }

        override fun onMenuItemSelected(item: MenuItem) = when (item.itemId) {
            R.id.action_debug -> {
                startActivity<DebugActivity>()
                true
            }

            R.id.action_settings -> {
                startActivity<SettingsActivity>()
                true
            }

            else -> false
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences = requireContext().defaultSharedPreferences
        activity?.addMenuProvider(menuProvider)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragTipsBinding
        .inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tipsEmptyStateCta.setOnClickListener {
            requireContext().launchUri(Constants.uriSubmitTip)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
