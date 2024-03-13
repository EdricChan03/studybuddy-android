package com.edricchan.studybuddy.exts.androidx.compose.foundation.lazy

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable

/**
 * Adds a list of items where the content of an item is aware of its index.
 *
 * This variant allows for a [dividerContent] to be specified to be shown between every item.
 * @param items the data list
 * @param key a factory of stable and unique keys representing the item.
 * Using the same key for multiple items in the list is not allowed.
 * Type of the key should be saveable via Bundle on Android.
 * If null is passed the position in the list will represent the key.
 * When you specify the key the scroll position will be maintained based on the key,
 * which means if you add/remove items before the current visible item the item with the
 * given key will be kept as the first visible one.
 * @param contentType a factory of the content types for the item.
 * The item compositions of the same type could be reused more efficiently.
 * Note that null is a valid type and items of such type will be considered compatible.
 * @param itemContent the content displayed by a single item
 * @param dividerContent the content displayed below each item except the last. Defaults to
 * a [HorizontalDivider] if not specified.
 * @see itemsIndexed
 * @see dividedItemsIndexed
 */
inline fun <T> LazyListScope.dividedItems(
    items: List<T>,
    noinline key: ((item: T) -> Any)? = null,
    noinline contentType: (item: T) -> Any? = { _ -> null },
    crossinline dividerContent: @Composable LazyItemScope.(item: T) -> Unit =
        { _ -> HorizontalDivider() },
    crossinline itemContent: @Composable LazyItemScope.(item: T) -> Unit
) = itemsIndexed(
    items = items,
    key = key?.let { { _: Int, item: T -> it(item) } },
    contentType = { _: Int, item: T -> contentType(item) },
    itemContent = { index, item ->
        itemContent(item)
        if (index < items.lastIndex) dividerContent(item)
    }
)

/**
 * Adds a list of items where the content of an item is aware of its index.
 *
 * This variant allows for a [dividerContent] to be specified to be shown between every item.
 * @param items the data list
 * @param key a factory of stable and unique keys representing the item.
 * Using the same key for multiple items in the list is not allowed.
 * Type of the key should be saveable via Bundle on Android.
 * If null is passed the position in the list will represent the key.
 * When you specify the key the scroll position will be maintained based on the key,
 * which means if you add/remove items before the current visible item the item with the
 * given key will be kept as the first visible one.
 * @param contentType a factory of the content types for the item.
 * The item compositions of the same type could be reused more efficiently.
 * Note that null is a valid type and items of such type will be considered compatible.
 * @param itemContent the content displayed by a single item
 * @param dividerContent the content displayed below each item except the last. Defaults to
 * a [HorizontalDivider] if not specified.
 * @see itemsIndexed
 */
inline fun <T> LazyListScope.dividedItemsIndexed(
    items: List<T>,
    noinline key: ((index: Int, item: T) -> Any)? = null,
    crossinline contentType: (index: Int, item: T) -> Any? = { _, _ -> null },
    crossinline dividerContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit =
        { _, _ -> HorizontalDivider() },
    crossinline itemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit
) = itemsIndexed(
    items = items, key = key, contentType = contentType
) { index, item ->
    itemContent(this, index, item)
    if (index < items.lastIndex) dividerContent(this, index, item)
}
