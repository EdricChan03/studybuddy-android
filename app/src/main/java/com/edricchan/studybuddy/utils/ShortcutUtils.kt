package com.edricchan.studybuddy.utils

import android.app.PendingIntent
import android.content.Context
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.os.Build
import androidx.core.content.getSystemService
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat

/**
 * Utility class to simplify the creation of app/pinned shortcuts.
 * @param context The context to be used for the shortcut manager(s).
 * @see android.content.pm.ShortcutManager
 * @see androidx.core.content.pm.ShortcutManagerCompat
 */
class ShortcutUtils(
    private val context: Context
) {
    /**
     * Creates a pinned shortcut with the given arguments.
     * @param id The ID to use.
     * @param requestCode The request code to use for the shortcut.
     */
    fun createPinnedShortcut(id: String, requestCode: Int) {
        createPinnedShortcut(id, requestCode) {}
    }

    /**
     * Creates a pinned shortcut with the given arguments.
     * @param id The ID to use.
     * @param requestCode The request code to use for the shortcut.
     * @param builderAction Actions to be passed to [ShortcutInfoCompat.Builder].
     */
    fun createPinnedShortcut(
        id: String, requestCode: Int,
        builderAction: ShortcutInfoCompat.Builder.() -> Unit
    ) {
        if (ShortcutManagerCompat.isRequestPinShortcutSupported(context)) {
            val pinShortcutInfo = ShortcutInfoCompat.Builder(context, id)
                .apply(builderAction)
                .build()

            val pinnedShortcutCallbackIntent = ShortcutManagerCompat.createShortcutResultIntent(
                context,
                pinShortcutInfo
            )

            val successCallback = PendingIntent.getBroadcast(
                context, requestCode,
                pinnedShortcutCallbackIntent, 0
            )

            ShortcutManagerCompat.requestPinShortcut(
                context,
                pinShortcutInfo,
                successCallback.intentSender
            )
        }
    }

    /**
     * @see android.content.pm.ShortcutManager.addDynamicShortcuts
     * @see ShortcutManagerCompat.addDynamicShortcuts
     * @see ShortcutInfoCompat
     */
    fun addDynamicShortcuts(shortcutInfoList: List<ShortcutInfoCompat>) {
        ShortcutManagerCompat.addDynamicShortcuts(context, shortcutInfoList)
    }

    /**
     * @see android.content.pm.ShortcutManager.setDynamicShortcuts
     * @see ShortcutInfo
     */
    fun setDynamicShortcuts(shortcutInfoList: List<ShortcutInfo>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            val shortcutManager = context.getSystemService<ShortcutManager>()
            shortcutManager?.dynamicShortcuts = shortcutInfoList
        }
    }

    /**
     * @see android.content.pm.ShortcutManager.getDynamicShortcuts
     * @see ShortcutManagerCompat.getDynamicShortcuts
     */
    fun getDynamicShortcuts() = ShortcutManagerCompat.getDynamicShortcuts(context)

    /**
     * @see android.content.pm.ShortcutManager.updateShortcuts
     * @see ShortcutManagerCompat.updateShortcuts
     * @see ShortcutInfoCompat
     */
    fun updateShortcuts(shortcutInfoList: List<ShortcutInfoCompat>) {
        ShortcutManagerCompat.updateShortcuts(context, shortcutInfoList)
    }

    /**
     * @see android.content.pm.ShortcutManager.removeDynamicShortcuts
     * @see ShortcutManagerCompat.removeDynamicShortcuts
     */
    fun removeDynamicShortcuts(shortcutIds: List<String>) {
        ShortcutManagerCompat.removeDynamicShortcuts(context, shortcutIds)
    }

    /**
     * @see android.content.pm.ShortcutManager.removeAllDynamicShortcuts
     * @see ShortcutManagerCompat.removeAllDynamicShortcuts
     */
    fun removeAllDynamicShortcuts() {
        ShortcutManagerCompat.removeAllDynamicShortcuts(context)
    }

    companion object {
        /**
         * Creates an instance of the [ShortcutUtils] class.
         * @param context The context to be used for this instance of the class.
         */
        fun getInstance(context: Context) = ShortcutUtils(context)
    }
}