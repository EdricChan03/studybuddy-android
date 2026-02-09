package com.edricchan.studybuddy.navigation.web

import android.net.Uri
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.get
import kotlin.reflect.KClass
import kotlin.reflect.KType

/**
 * Adds a [Custom Tab][androidx.browser.customtabs.CustomTabsIntent] to the [NavGraphBuilder].
 *
 * **Warning: This assumes that the [CustomTabsNavigator] has been added to the receiver's
 * [NavGraphBuilder.provider].**
 * @param route Route for the destination.
 * @param uri The URI to be opened.
 * @param applyAppDefaults Whether StudyBuddy's default options should be specified for the
 * [androidx.browser.customtabs.CustomTabsIntent.Builder].
 * @param arguments List of arguments to associate with the destination.
 * @param deepLinks List of deep links to associate with the destination.
 */
fun NavGraphBuilder.customTab(
    route: String,
    uri: Uri,
    applyAppDefaults: Boolean = true,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList()
) {
    val navigator = provider[CustomTabsNavigator::class]
    addDestination(
        CustomTabsNavigator.Destination(
            navigator.context,
            applyAppDefaults = applyAppDefaults,
            uri = uri,
            navigator = navigator
        ).apply {
            this.route = route
            arguments.forEach { (argName, argument) ->
                addArgument(argName, argument)
            }
            deepLinks.forEach { deepLink ->
                addDeepLink(deepLink)
            }
        }
    )
}

/**
 * Adds a [Custom Tab][androidx.browser.customtabs.CustomTabsIntent] to the [NavGraphBuilder].
 *
 * **Warning: This assumes that the [CustomTabsNavigator] has been added to the receiver's
 * [NavGraphBuilder.provider].**
 * @param route Route for the destination.
 * @param uri The URI to be opened.
 * @param builder Configures the [destination][CustomTabsNavigator.Destination].
 * @see CustomTabsNavigatorDestinationBuilder
 */
fun NavGraphBuilder.customTab(
    route: String,
    uri: Uri,
    builder: CustomTabsNavigatorDestinationBuilder.() -> Unit
) {
    destination(
        CustomTabsNavigatorDestinationBuilder(
            provider[CustomTabsNavigator::class],
            route,
            uri
        ).apply(builder)
    )
}

/**
 * Adds a [Custom Tab][androidx.browser.customtabs.CustomTabsIntent] to the [NavGraphBuilder].
 *
 * **Warning: This assumes that the [CustomTabsNavigator] has been added to the receiver's
 * [NavGraphBuilder.provider].**
 * @param route Route for the destination.
 * @param uri The URI to be opened.
 * @param builder Configures the [destination][CustomTabsNavigator.Destination].
 * @see CustomTabsNavigatorDestinationBuilder
 */
inline fun <T : Any> NavGraphBuilder.customTab(
    route: KClass<T>,
    uri: Uri,
    typeMap: Map<KType, @JvmSuppressWildcards NavType<*>> = mapOf(),
    builder: CustomTabsNavigatorDestinationBuilder.() -> Unit = {}
) {
    destination(
        CustomTabsNavigatorDestinationBuilder(
            provider[CustomTabsNavigator::class],
            route,
            typeMap,
            uri
        ).apply(builder)
    )
}

/**
 * Adds a [Custom Tab][androidx.browser.customtabs.CustomTabsIntent] to the [NavGraphBuilder].
 *
 * **Warning: This assumes that the [CustomTabsNavigator] has been added to the receiver's
 * [NavGraphBuilder.provider].**
 * @param T Route for the destination.
 * @param uri The URI to be opened.
 * @param builder Configures the [destination][CustomTabsNavigator.Destination].
 * @see CustomTabsNavigatorDestinationBuilder
 */
inline fun <reified T : Any> NavGraphBuilder.customTab(
    uri: Uri,
    typeMap: Map<KType, @JvmSuppressWildcards NavType<*>> = mapOf(),
    builder: CustomTabsNavigatorDestinationBuilder.() -> Unit = {}
) {
    destination(
        CustomTabsNavigatorDestinationBuilder(
            provider[CustomTabsNavigator::class],
            T::class,
            typeMap,
            uri
        ).apply(builder)
    )
}
