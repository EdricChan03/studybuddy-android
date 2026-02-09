package com.edricchan.studybuddy.navigation.web

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsSession
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import com.edricchan.studybuddy.utils.web.applyAppDefaults
import io.github.edricchan03.androidx.browser.ktx.customTabsIntentBuilder
import io.github.edricchan03.androidx.browser.ktx.enums.ActivityHeightResizeBehavior
import io.github.edricchan03.androidx.browser.ktx.enums.CloseButtonPosition
import io.github.edricchan03.androidx.browser.ktx.enums.ColorScheme
import io.github.edricchan03.androidx.browser.ktx.enums.ShareState
import io.github.edricchan03.androidx.browser.ktx.setCloseButtonPosition
import io.github.edricchan03.androidx.browser.ktx.setColorScheme
import io.github.edricchan03.androidx.browser.ktx.setDefaultColorSchemeParams
import io.github.edricchan03.androidx.browser.ktx.setInitialActivityHeightPx
import io.github.edricchan03.androidx.browser.ktx.setShareState

/**
 * Navigator which allows for routes to a Custom Tab to be made.
 *
 * Based from [this implementation](https://www.oliverspryn.com/blog/add-chrome-custom-tabs-to-the-android-jetpack-navigation-component)
 */
@Navigator.Name("custom-tab")
class CustomTabsNavigator(
    internal val context: Context, // Marked as internal as it's used by the destination builder
    private val applyAppDefaults: Boolean = true
) : Navigator<CustomTabsNavigator.Destination>() {
    override fun createDestination() = Destination(
        context, applyAppDefaults, Uri.EMPTY, this
    )

    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Extras?
    ): NavDestination {
        val intent = destination.buildCustomTabsIntent()
        intent.launchUrl(context, destination.uri)
        return destination
    }

    @NavDestination.ClassType(Activity::class)
    class Destination(
        private val context: Context,
        private val applyAppDefaults: Boolean = true,
        internal val uri: Uri,
        navigator: Navigator<out NavDestination>
    ) : NavDestination(navigator) {
        /**
         * Sets the custom tab's toolbar colour using a colour integer.
         * @see androidx.browser.customtabs.CustomTabColorSchemeParams.toolbarColor
         */
        @ColorInt
        var toolbarColor: Int? = null

        /** Sets the custom tab's toolbar colour using a colour resource. */
        fun setToolbarColorResource(@ColorRes color: Int) {
            toolbarColor = ContextCompat.getColor(context, color)
        }

        /**
         * Sets the custom tab's secondary toolbar colour using a colour integer.
         * @see androidx.browser.customtabs.CustomTabColorSchemeParams.secondaryToolbarColor
         */
        @ColorInt
        var secondaryToolbarColor: Int? = null

        /** Sets the custom tab's secondary toolbar colour using a colour resource. */
        fun setSecondaryToolbarColorResource(@ColorRes color: Int) {
            secondaryToolbarColor = ContextCompat.getColor(context, color)
        }

        /**
         * Sets the system navigation bar's colour using a colour integer.
         * @see androidx.browser.customtabs.CustomTabColorSchemeParams.navigationBarColor
         */
        @ColorInt
        var navigationBarColor: Int? = null

        /** Sets the system navigation bar's colour using a colour resource. */
        fun setNavigationBarColorResource(@ColorRes color: Int) {
            navigationBarColor = ContextCompat.getColor(context, color)
        }

        /**
         * Sets the system navigation bar divider's colour using a colour integer.
         * @see androidx.browser.customtabs.CustomTabColorSchemeParams.navigationBarDividerColor
         */
        @ColorInt
        var navigationBarDividerColor: Int? = null

        /** Sets the system navigation bar divider's colour using a colour resource. */
        fun setNavigationBarDividerColorResource(@ColorRes color: Int) {
            navigationBarDividerColor = ContextCompat.getColor(context, color)
        }

        /**
         * Sets the share state that should be applied to the custom tab.
         * @see setShareState
         */
        var shareState = ShareState.Default

        /**
         * Sets the colour scheme that should be applied to the user interface in the custom tab.
         * @see setColorScheme
         */
        var colorScheme = ColorScheme.System

        /**
         * Sets the Custom Tab Activity's initial height in pixels and the desired resize behavior.
         * The Custom Tab will behave as a bottom sheet.
         * @see androidx.browser.customtabs.CustomTabsIntent.Builder.setInitialActivityHeightPx
         */
        @Dimension(unit = Dimension.PX)
        var initialActivityHeightPx = 0

        /**
         * Sets the Custom Tab Activity's desired resize behavior.
         * @see setInitialActivityHeightPx
         */
        var activityHeightResizeBehavior = ActivityHeightResizeBehavior.Default

        /**
         * Sets the position of the close button.
         * @see setCloseButtonPosition
         */
        var closeButtonPosition = CloseButtonPosition.Default

        /**
         * Sets whether the title should be shown in the custom tab.
         * @see androidx.browser.customtabs.CustomTabsIntent.Builder.setShowTitle
         */
        var showTitle = true

        /** Sets an existing session to use, if any. */
        var session: CustomTabsSession? = null

        private val builder = customTabsIntentBuilder(session)

        /** Configures the [CustomTabsIntent] using DSL syntax. */
        fun configureCustomTabsIntent(builderInit: CustomTabsIntent.Builder.() -> Unit = {}) {
            builder.apply(builderInit)
        }

        internal fun buildCustomTabsIntent(): CustomTabsIntent {
            configureCustomTabsIntent {
                setDefaultColorSchemeParams(
                    toolbarColor = toolbarColor,
                    secondaryToolbarColor = secondaryToolbarColor,
                    navigationBarColor = navigationBarColor,
                    navigationBarDividerColor = navigationBarDividerColor
                )
                setShareState(shareState)
                setColorScheme(colorScheme)
                initialActivityHeightPx.takeIf { it > 0 }?.let {
                    setInitialActivityHeightPx(
                        it,
                        activityHeightResizeBehavior
                    )
                }
                setCloseButtonPosition(closeButtonPosition)
                setShowTitle(showTitle)

                if (applyAppDefaults) applyAppDefaults(context)
            }
            return builder.build()
        }

        override fun onInflate(context: Context, attrs: AttributeSet) {
            super.onInflate(context, attrs)

            context.withStyledAttributes(attrs, R.styleable.CustomTabsNavigator, 0, 0) {
                setToolbarColorResource(
                    getResourceId(
                        R.styleable.CustomTabsNavigator_ct_toolbarColor,
                        0
                    )
                )
                setSecondaryToolbarColorResource(
                    getResourceId(
                        R.styleable.CustomTabsNavigator_ct_secondaryToolbarColor,
                        0
                    )
                )
                setNavigationBarColorResource(
                    getResourceId(
                        R.styleable.CustomTabsNavigator_ct_navigationBarColor,
                        0
                    )
                )
                setNavigationBarDividerColorResource(
                    getResourceId(
                        R.styleable.CustomTabsNavigator_ct_navigationBarDividerColor,
                        0
                    )
                )
                shareState = ShareState.fromValue(
                    getString(R.styleable.CustomTabsNavigator_ct_shareState)?.toIntOrNull() ?: 0
                )
                colorScheme = ColorScheme.fromValue(
                    getString(R.styleable.CustomTabsNavigator_ct_colorScheme)?.toIntOrNull() ?: 0
                )
                initialActivityHeightPx = getDimensionPixelSize(
                    R.styleable.CustomTabsNavigator_ct_initialActivityHeightPx,
                    0
                )
                activityHeightResizeBehavior = ActivityHeightResizeBehavior.fromValue(
                    getString(
                        R.styleable.CustomTabsNavigator_ct_activityHeightResizeBehavior
                    )?.toIntOrNull() ?: 0
                )
                closeButtonPosition = CloseButtonPosition.fromValue(
                    getString(
                        R.styleable.CustomTabsNavigator_ct_closeButtonPosition
                    )?.toIntOrNull() ?: 0
                )
                showTitle = getBoolean(R.styleable.CustomTabsNavigator_ct_showTitle, true)
            }
        }
    }
}
