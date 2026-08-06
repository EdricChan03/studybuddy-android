package com.edricchan.studybuddy.utils.compose.material3.list

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ListItemShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

enum class SegmentedListItemPosition {
    /**
     * Denotes a single list item in a segmented list.
     *
     * Note that this is equivalent to [Middle] when passed to [segmentedShapes].
     */
    Single,

    /** Denotes the first list item in a segmented list. */
    First,

    /** Denotes the middle list items in a segmented list. */
    Middle,

    /** Denotes the last list item in a segmented list. */
    Last;

    companion object {
        fun fromIndex(index: Int, size: Int): SegmentedListItemPosition = when {
            size <= 1 -> Single
            index == 0 -> First
            index == size - 1 -> Last
            else -> Middle
        }
    }
}

/**
 * Creates the desired [ListItemShapes] for a [androidx.compose.material3.SegmentedListItem]
 * based on its [position].
 * @param position The [SegmentedListItemPosition] for the specific
 * [androidx.compose.material3.SegmentedListItem].
 * @param defaultShapes The default [ListItemShapes] that should be used for standalone items
 * or items in the middle of the list.
 * @see androidx.compose.material3.ListItemDefaults.segmentedShapes
 */
@Composable
fun ListItemDefaults.segmentedShapes(
    position: SegmentedListItemPosition,
    defaultShapes: ListItemShapes = shapes()
): ListItemShapes {
    val overrideShape = MaterialTheme.shapes.large

    return remember(position, defaultShapes) {
        when (position) {
            SegmentedListItemPosition.First -> {
                val defaultBaseShape = defaultShapes.shape
                if (defaultBaseShape is CornerBasedShape) {
                    defaultShapes.copy(
                        shape = defaultBaseShape.copy(
                            topStart = overrideShape.topStart,
                            topEnd = overrideShape.topEnd,
                        )
                    )
                } else {
                    defaultShapes
                }
            }

            SegmentedListItemPosition.Last -> {
                val defaultBaseShape = defaultShapes.shape
                if (defaultBaseShape is CornerBasedShape) {
                    defaultShapes.copy(
                        shape = defaultBaseShape.copy(
                            bottomStart = overrideShape.bottomStart,
                            bottomEnd = overrideShape.bottomEnd,
                        )
                    )
                } else {
                    defaultShapes
                }
            }

            else -> defaultShapes
        }
    }
}
