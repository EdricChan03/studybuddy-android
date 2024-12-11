package io.github.edricchan03.optionbottomsheet.models.dsl

import androidx.compose.runtime.Composable
import io.github.edricchan03.optionbottomsheet.models.BottomSheetOption
import io.github.edricchan03.optionbottomsheet.models.BottomSheetOptionGroup
import io.github.edricchan03.optionbottomsheet.models.BottomSheetOptionGroupCheckedChangeListener
import kotlin.properties.Delegates

@DslMarker
annotation class OptionBottomSheetDsl

@OptionBottomSheetDsl
class BottomSheetOptionGroupItemBuilder(val id: Int) {
    /** @see BottomSheetOption.title */
    var title by Delegates.notNull<String>()

    /** @see BottomSheetOption.icon */
    var icon: (@Composable () -> Unit)? = null

    /** @see BottomSheetOption.icon */
    fun icon(icon: (@Composable () -> Unit)? = null) {
        this.icon = icon
    }

    /** @see BottomSheetOption.onClick */
    var onClick: () -> Unit = {}

    /** @see BottomSheetOption.visible */
    var visible = true

    /** @see BottomSheetOption.enabled */
    var enabled = true

    /**
     * Whether this option should send a request to dismiss the containing
     * modal bottom sheet when clicked on.
     * @see BottomSheetOption.requestDismissOnClick
     */
    var requestDismissOnClick = true

    /** Whether this option should be selected within the group. */
    var selected = false

    /** Creates the [BottomSheetOption] with this class' options. */
    fun build() = BottomSheetOption(
        id = id,
        title = title,
        icon = icon,
        onClick = onClick,
        visible = visible,
        enabled = enabled,
        requestDismissOnClick = requestDismissOnClick
    )
}

@OptionBottomSheetDsl
class BottomSheetOptionGroupBuilder {
    private val itemSelectionMap = mutableMapOf<BottomSheetOption, Boolean>()

    /**
     * Adds an item to this option group.
     * @param id The ID to use. This value should be unique.
     * @param init Configuration options for the option group item.
     */
    @Composable
    fun item(
        id: Int,
        init: @Composable BottomSheetOptionGroupItemBuilder.() -> Unit
    ): BottomSheetOption {
        val builder = BottomSheetOptionGroupItemBuilder(id).apply { init() }
        return builder.build().also {
            itemSelectionMap += it to builder.selected
        }
    }

    /**
     * Adds the list of [items] to this option group.
     *
     * [itemInit] is used to determine how to add an item, see [item].
     * @param items The list of items to be added.
     * @param itemId Lambda used to map an item [T] to its ID form. Defaults to
     * the index if not specified.
     * @param itemInit Lambda used to create an item from [T].
     * @see item
     */
    @Composable
    fun <T> items(
        items: List<T>,
        itemId: (IndexedValue<T>) -> Int = IndexedValue<T>::index,
        itemInit: @Composable BottomSheetOptionGroupItemBuilder.(T) -> Unit
    ): List<BottomSheetOption> = items.withIndex().map {
        item(id = itemId(it)) {
            itemInit(it.value)
        }
    }

    // Group properties
    var title: String? = null
    var visible = true
    var enabled = true
    var onCheckedChange: BottomSheetOptionGroupCheckedChangeListener = { _, _ -> }
    var checkableBehavior = BottomSheetOptionGroup.CheckableBehavior.None

    fun build() = BottomSheetOptionGroup(
        title = title,
        itemsSelectionMap = itemSelectionMap,
        visible = visible,
        enabled = enabled,
        onCheckedChange = onCheckedChange,
        checkableBehavior = checkableBehavior
    )
}

/** Creates a new [BottomSheetOptionGroup] given the specified parameters. */
@OptionBottomSheetDsl
@Composable
fun optionGroup(init: @Composable BottomSheetOptionGroupBuilder.() -> Unit) =
    BottomSheetOptionGroupBuilder().apply { init() }.build()
