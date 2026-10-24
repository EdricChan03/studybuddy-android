package com.edricchan.studybuddy.core.di.testing.fragment

import android.content.ComponentName
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.commitNow
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import com.edricchan.studybuddy.core.di.testing.HiltTestActivity
import com.edricchan.studybuddy.ui.theming.R

inline fun <reified T : Fragment> launchHiltFragmentInContainer(
    fragmentArgs: Bundle? = null,
    @StyleRes themeResId: Int = R.style.Theme_App,
    @IdRes containerResId: Int = android.R.id.content,
    initialState: Lifecycle.State = Lifecycle.State.RESUMED,
    crossinline action: T.() -> Unit = {}
) {
    val startActivityIntent = Intent.makeMainActivity(
        ComponentName(
            ApplicationProvider.getApplicationContext(),
            HiltTestActivity::class.java
        )
    ).putExtra(
        HiltTestActivity.THEME_EXTRAS_BUNDLE_KEY,
        themeResId
    )

    launchActivity<HiltTestActivity>(startActivityIntent).onActivity { activity ->
        val fragment = activity.supportFragmentManager.fragmentFactory.instantiate(
            checkNotNull(T::class.java.classLoader),
            T::class.java.name
        ) as T
        fragment.arguments = fragmentArgs
        activity.supportFragmentManager.commitNow {
            add(containerResId, fragment, "")
            setMaxLifecycle(fragment, initialState)
        }

        fragment.action()
    }
}

inline fun <reified T : Fragment> launchHiltFragment(
    fragmentArgs: Bundle? = null,
    @StyleRes themeResId: Int = R.style.Theme_App,
    initialState: Lifecycle.State = Lifecycle.State.RESUMED,
    crossinline action: T.() -> Unit = {}
) = launchHiltFragmentInContainer(
    fragmentArgs = fragmentArgs,
    themeResId = themeResId,
    containerResId = Resources.ID_NULL,
    initialState = initialState,
    action = action
)
