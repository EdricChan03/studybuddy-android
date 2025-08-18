package com.edricchan.studybuddy.exts.androidx.lifecycle.serialization

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.serialization.saved
import androidx.savedstate.serialization.SavedStateConfiguration
import androidx.savedstate.serialization.serializers.MutableStateFlowSerializer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import kotlin.properties.ReadWriteProperty

/**
 * Returns a property delegate that uses [SavedStateHandle] to save and restore a
 * [MutableStateFlow]-backed value of type [T] with the default serializer.
 *
 * @see saved
 * @param key An optional [String] key to use for storing the value in the [SavedStateHandle]. A
 *   default key will be generated if it's omitted or when 'null' is passed.
 * @param configuration The [SavedStateConfiguration] to use. Defaults to
 *   [SavedStateConfiguration.DEFAULT].
 * @param initialValue The function to provide the initial value of the backing [MutableStateFlow].
 * @return A property delegate that manages the saving and restoring of the [MutableStateFlow]
 * value.
 */
inline fun <reified T> SavedStateHandle.savedMutableStateFlow(
    valueSerializer: KSerializer<T> = serializer(),
    key: String? = null,
    configuration: SavedStateConfiguration = SavedStateConfiguration.DEFAULT,
    crossinline initialValue: () -> T
): ReadWriteProperty<Any?, MutableStateFlow<T>> = saved(
    key = key,
    serializer = MutableStateFlowSerializer(valueSerializer),
    configuration = configuration
) { MutableStateFlow(initialValue()) }

/**
 * Returns a property delegate that uses [SavedStateHandle] to save and restore a
 * [MutableStateFlow]-backed value of type [T] with the default serializer.
 *
 * @see saved
 * @param key An optional [String] key to use for storing the value in the [SavedStateHandle]. A
 *   default key will be generated if it's omitted or when 'null' is passed.
 * @param configuration The [SavedStateConfiguration] to use. Defaults to
 *   [SavedStateConfiguration.DEFAULT].
 * @param initialValue The initial value of the backing [MutableStateFlow].
 * @return A property delegate that manages the saving and restoring of the [MutableStateFlow]
 * value.
 */
inline fun <reified T> SavedStateHandle.savedMutableStateFlow(
    valueSerializer: KSerializer<T> = serializer(),
    key: String? = null,
    configuration: SavedStateConfiguration = SavedStateConfiguration.DEFAULT,
    initialValue: T
): ReadWriteProperty<Any?, MutableStateFlow<T>> = savedMutableStateFlow(
    valueSerializer = valueSerializer,
    key = key,
    configuration = configuration,
    initialValue = { initialValue }
)
