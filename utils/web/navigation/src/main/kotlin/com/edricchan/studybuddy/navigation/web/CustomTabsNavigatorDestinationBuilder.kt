package com.edricchan.studybuddy.navigation.web

import android.content.Context
import android.net.Uri
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.navigation.NavDestinationBuilder
import androidx.navigation.NavDestinationDsl
import androidx.navigation.NavType
import io.github.edricchan03.androidx.browser.ktx.enums.ActivityHeightResizeBehavior
import io.github.edricchan03.androidx.browser.ktx.enums.CloseButtonPosition
import io.github.edricchan03.androidx.browser.ktx.enums.ColorScheme
import io.github.edricchan03.androidx.browser.ktx.enums.ShareState
import kotlin.reflect.KClass
import kotlin.reflect.KType

@NavDestinationDsl
class CustomTabsNavigatorDestinationBuilder :
    NavDestinationBuilder<CustomTabsNavigator.Destination> {
    private val uri: Uri

    constructor(
        navigator: CustomTabsNavigator,
        route: String,
        uri: Uri
    ) : super(navigator, route) {
        this.uri = uri
        this.context = navigator.context
    }

    constructor(
        navigator: CustomTabsNavigator,
        route: KClass<*>?,
        typeMap: Map<KType, @JvmSuppressWildcards NavType<*>>,
        uri: Uri
    ) : super(navigator, route, typeMap) {
        this.uri = uri
        this.context = navigator.context
    }

    private val context: Context

    /**
     * Sets the custom tab's toolbar colour using a colour integer.
     * @see androidx.browser.customtabs.CustomTabColorSchemeParams.toolbarColor
     */
    @ColorInt
    var toolbarColor = 0

    /** Sets the custom tab's toolbar colour using a colour resource. */
    fun setToolbarColorResource(@ColorRes color: Int) {
        toolbarColor = ContextCompat.getColor(context, color)
    }

    /**
     * Sets the custom tab's secondary toolbar colour using a colour integer.
     * @see androidx.browser.customtabs.CustomTabColorSchemeParams.secondaryToolbarColor
     */
    @ColorInt
    var secondaryToolbarColor = 0

    /** Sets the custom tab's secondary toolbar colour using a colour resource. */
    fun setSecondaryToolbarColorResource(@ColorRes color: Int) {
        secondaryToolbarColor = ContextCompat.getColor(context, color)
    }

    /**
     * Sets the system navigation bar's colour using a colour integer.
     * @see androidx.browser.customtabs.CustomTabColorSchemeParams.navigationBarColor
     */
    @ColorInt
    var navigationBarColor = 0

    /** Sets the system navigation bar's colour using a colour resource. */
    fun setNavigationBarColorResource(@ColorRes color: Int) {
        navigationBarColor = ContextCompat.getColor(context, color)
    }

    /**
     * Sets the system navigation bar divider's colour using a colour integer.
     * @see androidx.browser.customtabs.CustomTabColorSchemeParams.navigationBarDividerColor
     */
    @ColorInt
    var navigationBarDividerColor = 0

    /** Sets the system navigation bar divider's colour using a colour resource. */
    fun setNavigationBarDividerColorResource(@ColorRes color: Int) {
        navigationBarDividerColor = ContextCompat.getColor(context, color)
    }

    /**
     * Sets the share state that should be applied to the custom tab.
     * @see io.github.edricchan03.androidx.browser.ktx.setShareState
     */
    var shareState = ShareState.Default

    /**
     * Sets the colour scheme that should be applied to the user interface in the custom tab.
     * @see io.github.edricchan03.androidx.browser.ktx.setColorScheme
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
     * @see io.github.edricchan03.androidx.browser.ktx.activityResizeBehavior
     */
    var activityHeightResizeBehavior = ActivityHeightResizeBehavior.Default

    /**
     * Sets the position of the close button.
     * @see io.github.edricchan03.androidx.browser.ktx.setCloseButtonPosition
     */
    var closeButtonPosition = CloseButtonPosition.Default

    private val configs: MutableList<CustomTabsIntent.Builder.() -> Unit> = mutableListOf()

    /** Additional configuration options to be set on the [CustomTabsIntent]. */
    fun additionalConfig(builderInit: CustomTabsIntent.Builder.() -> Unit) {
        configs += builderInit
    }

    override fun build(): CustomTabsNavigator.Destination {
        return super.build().also { destination ->
//            destination.uri = uri
            destination.toolbarColor = toolbarColor
            destination.secondaryToolbarColor = secondaryToolbarColor
            destination.navigationBarColor = navigationBarColor
            destination.navigationBarDividerColor = navigationBarDividerColor
            destination.shareState = shareState
            destination.colorScheme = colorScheme
            destination.initialActivityHeightPx = initialActivityHeightPx
            destination.activityHeightResizeBehavior = activityHeightResizeBehavior
            destination.closeButtonPosition = closeButtonPosition
            configs.forEach {
                destination.configureCustomTabsIntent(it)
            }
        }
    }
}
