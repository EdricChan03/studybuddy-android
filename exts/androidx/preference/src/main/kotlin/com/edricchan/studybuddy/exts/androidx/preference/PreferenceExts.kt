package com.edricchan.studybuddy.exts.androidx.preference

import androidx.fragment.app.Fragment
import androidx.preference.Preference
import kotlin.reflect.KClass

/**
 * Sets the class name of a fragment to be shown when this preference is clicked.
 *
 * This is equivalent to setting `fragment` to the value `fragmentClass.qualifiedName`.
 * @see Preference.setFragment
 */
fun <F : Fragment> Preference.setFragment(fragmentClass: KClass<F>) {
    // We use java.lang.Class.getName which correctly resolves nested classes
    // For example: <...>.settings.fragment.ExampleSettingsFragment.ExampleSettingsDataFragment
    // becomes <...>.settings.fragment.ExampleSettingsFragment$ExampleSettingsDataFragment
    fragment = fragmentClass.java.name
}

/**
 * Sets the class name of a fragment to be shown when this preference is clicked.
 *
 * This is equivalent to setting `fragment` to the value `F::class.qualifiedName`.
 * @see Preference.setFragment
 */
inline fun <reified F : Fragment> Preference.setFragment() {
    setFragment(F::class)
}

/**
 * Sets the class name of a fragment to be shown when this preference is clicked.
 *
 * This is equivalent to calling `setFragment(fragmentClassProducer())`.
 * @see Preference.setFragment
 */
fun <F : Fragment> Preference.setFragment(fragmentClassProducer: () -> KClass<F>) {
    setFragment(fragmentClassProducer())
}
