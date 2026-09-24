package com.edricchan.studybuddy.ui.widgets.compose.form.chips.data

import android.os.Parcelable
import com.edricchan.studybuddy.ui.widgets.compose.form.chips.OutlinedChipTextField
import kotlinx.parcelize.Parcelize

/** Data representation for a chip to be displayed in a [OutlinedChipTextField]. */
interface ChipItem : Parcelable {
    /** The desired text to be displayed for this chip. */
    val text: String

    /** Whether one chip is equal to another. */
    override fun equals(other: Any?): Boolean

    /** Factory for creating dedicated [C] instances. */
    fun interface Factory<C : ChipItem> {
        /** Creates a [C] given the specified [textFieldValue]. */
        fun create(textFieldValue: String): C
    }
}

/** Default implementation of [ChipItem] which does not render additional content. */
@Parcelize
data class DefaultChipItem(
    override val text: String
) : ChipItem {
    companion object {
        val Factory = ChipItem.Factory {
            DefaultChipItem(it)
        }
    }
}

/**
 * Uses reflection to create a [ChipItem.Factory] for the reified [C] type.
 *
 * This method expects that the given [C] class has a public constructor that
 * takes in one argument being the [ChipItem.text].
 *
 * Note that this may fail at runtime - see [Class.getDeclaredConstructor] for
 * the possible exceptions.
 */
inline fun <reified C : ChipItem> chipItemFactory(): ChipItem.Factory<C> = ChipItem.Factory {
    C::class.java.getDeclaredConstructor(String::class.java).newInstance(it)
}
