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
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.core.compat.navigation.navigateToDebug
import com.edricchan.studybuddy.core.compat.navigation.navigateToSettings
import com.edricchan.studybuddy.databinding.FragTipsBinding
import com.edricchan.studybuddy.exts.androidx.preference.defaultSharedPreferences
import com.edricchan.studybuddy.exts.androidx.viewbinding.viewInflateBinding
import com.edricchan.studybuddy.utils.web.launchUri

class TipsFragment : Fragment() {
    private var preferences: SharedPreferences? = null

    private lateinit var navController: NavController

    private val binding by viewInflateBinding(FragTipsBinding::inflate)

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
                navController.navigateToDebug()
                true
            }

            R.id.action_settings -> {
                navController.navigateToSettings()
                true
            }

            else -> false
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences = requireContext().defaultSharedPreferences

        navController = findNavController()
        activity?.addMenuProvider(menuProvider)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tipsEmptyStateCta.setOnClickListener {
            requireContext().launchUri(Constants.uriSubmitTip)
        }
    }
}
