// This file provides workarounds for using type-safe navigation in the NavDeepLinkBuilder;
// see below for tracking bug
// TODO: Remove this file when https://issuetracker.google.com/issues/345125731 is fixed
package com.edricchan.studybuddy.exts.androidx.navigation.runtime

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.TaskStackBuilder
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.serialization.generateHashCode
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

/**
 * Sets the type-safe destination to deep link to.
 * Any destinations previous added via [addDestination] are cleared,
 * effectively resetting this object back to only this single destination.
 * @param destSerializer [KSerializer] of the type-safe destination.
 */
@SuppressLint("RestrictedApi")
fun <T : Any> NavDeepLinkBuilder.setDestination(destSerializer: KSerializer<T>): NavDeepLinkBuilder =
    setDestination(destSerializer.generateHashCode())

/**
 * Sets the type-safe destination to deep link to.
 * Any destinations previous added via [addDestination] are cleared,
 * effectively resetting this object back to only this single destination.
 * @param dest The type-safe destination.
 */
@OptIn(InternalSerializationApi::class)
fun <T : Any> NavDeepLinkBuilder.setDestination(dest: T): NavDeepLinkBuilder =
    setDestination(dest::class.serializer())

/**
 * Sets the type-safe destination to deep link to.
 * Any destinations previous added via [addDestination] are cleared,
 * effectively resetting this object back to only this single destination.
 * @param dest [KClass] of the type-safe destination.
 */
@OptIn(InternalSerializationApi::class)
fun <T : Any> NavDeepLinkBuilder.setDestination(dest: KClass<T>): NavDeepLinkBuilder =
    setDestination(dest.serializer())

/**
 * Add a new destination id to deep link to.
 * This builds off any previous calls to this method or calls to [setDestination], building
 * the minimal synthetic back stack of start destinations between the previous deep link
 * destination and the newly added deep link destination.
 * @param destSerializer [KSerializer] of the type-safe destination.
 */
@SuppressLint("RestrictedApi")
fun <T : Any> NavDeepLinkBuilder.addDestination(destSerializer: KSerializer<T>): NavDeepLinkBuilder =
    addDestination(destSerializer.generateHashCode())

/**
 * Add a new destination id to deep link to.
 * This builds off any previous calls to this method or calls to [setDestination], building
 * the minimal synthetic back stack of start destinations between the previous deep link
 * destination and the newly added deep link destination.
 * @param dest The type-safe destination.
 */
@OptIn(InternalSerializationApi::class)
fun <T : Any> NavDeepLinkBuilder.addDestination(dest: T): NavDeepLinkBuilder =
    addDestination(dest::class.serializer())

@OptIn(InternalSerializationApi::class)
fun <T : Any> NavDeepLinkBuilder.addDestination(dest: KClass<T>): NavDeepLinkBuilder =
    addDestination(dest.serializer())

/**
 * Creates a [PendingIntent] using the specified [NavDeepLinkBuilder] options.
 * @param init DSL to configure the [NavDeepLinkBuilder] with.
 */
fun Context.createNavPendingIntent(init: NavDeepLinkBuilder.() -> Unit): PendingIntent =
    NavDeepLinkBuilder(this).apply(init).createPendingIntent()

/**
 * Creates a [TaskStackBuilder] using the specified [NavDeepLinkBuilder] options.
 * @param init DSL to configure the [NavDeepLinkBuilder] with.
 */
fun Context.createNavTaskStackBuilder(init: NavDeepLinkBuilder.() -> Unit): TaskStackBuilder =
    NavDeepLinkBuilder(this).apply(init).createTaskStackBuilder()
