package com.edricchan.studybuddy.utils.androidx.core

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.annotation.MenuRes
import androidx.core.view.MenuProvider

/**
 * Creates a [MenuProvider] with the specified callback lambdas.
 * @param onCreateMenu Callback to be invoked by [MenuProvider.onCreateMenu].
 * @param onPrepareMenu Callback to be invoked by [MenuProvider.onPrepareMenu].
 * @param onMenuClosed Callback to be invoked by [MenuProvider.onMenuClosed].
 * @param onMenuItemSelected Callback to be invoked by [MenuProvider.onMenuItemSelected].
 */
inline fun menuProvider(
    crossinline onCreateMenu: (menu: Menu, menuInflater: MenuInflater) -> Unit,
    crossinline onPrepareMenu: (Menu) -> Unit = {},
    crossinline onMenuClosed: (Menu) -> Unit = {},
    crossinline onMenuItemSelected: (MenuItem) -> Boolean,
): MenuProvider = object : MenuProvider {
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        onCreateMenu(menu, menuInflater)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return onMenuItemSelected(menuItem)
    }

    override fun onMenuClosed(menu: Menu) {
        onMenuClosed(menu)
    }

    override fun onPrepareMenu(menu: Menu) {
        onPrepareMenu(menu)
    }
}

/**
 * Creates a [MenuProvider] with the specified [menuResId] and callback lambdas.
 * @param menuResId Desired menu resource to be inflated in [MenuProvider.onCreateMenu].
 * @param onCreateMenu Callback to be invoked by [MenuProvider.onCreateMenu]. By default, it
 * calls [MenuInflater.inflate] with the [Menu] and [menuResId].
 * @param onPrepareMenu Callback to be invoked by [MenuProvider.onPrepareMenu].
 * @param onMenuClosed Callback to be invoked by [MenuProvider.onMenuClosed].
 * @param onMenuItemSelected Callback to be invoked by [MenuProvider.onMenuItemSelected].
 */
inline fun menuProvider(
    @MenuRes menuResId: Int,
    crossinline onCreateMenu: (menu: Menu, menuInflater: MenuInflater) -> Unit = { menu, menuInflater ->
        menuInflater.inflate(menuResId, menu)
    },
    crossinline onPrepareMenu: (Menu) -> Unit = {},
    crossinline onMenuClosed: (Menu) -> Unit = {},
    crossinline onMenuItemSelected: (MenuItem) -> Boolean,
): MenuProvider = menuProvider(
    onCreateMenu = onCreateMenu,
    onPrepareMenu = onPrepareMenu,
    onMenuClosed = onMenuClosed,
    onMenuItemSelected = onMenuItemSelected
)
