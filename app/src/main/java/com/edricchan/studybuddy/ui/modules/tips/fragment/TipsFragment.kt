package com.edricchan.studybuddy.ui.modules.tips.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.core.compat.navigation.navigateToDebug
import com.edricchan.studybuddy.core.compat.navigation.navigateToSettings
import com.edricchan.studybuddy.databinding.FragTipsBinding
import com.edricchan.studybuddy.ui.common.fragment.ViewBindingFragment
import com.edricchan.studybuddy.utils.web.launchUri

class TipsFragment : ViewBindingFragment<FragTipsBinding>(FragTipsBinding::inflate) {
    override val menuProvider = object : MenuProvider {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tipsEmptyStateCta.setOnClickListener {
            requireContext().launchUri(Constants.uriSubmitTip)
        }
    }
}
