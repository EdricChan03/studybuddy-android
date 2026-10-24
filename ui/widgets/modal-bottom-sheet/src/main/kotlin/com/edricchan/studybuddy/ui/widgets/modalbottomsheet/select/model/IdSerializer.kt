package com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model

import kotlin.enums.EnumEntries
import kotlin.enums.enumEntries

/** Serialiser for serialising between an [Id] and its [String] equivalent. */
abstract class IdSerializer<Id : Any> {
    /** Serialise the given [id] to its [String] equivalent. */
    open fun serialize(id: Id): String = id.toString()

    /**
     * De-serialise the given [rawValue] string to its [Id] equivalent.
     *
     * Implementations may return `null` if the given [rawValue] could not be de-serialised.
     */
    abstract fun deserialize(rawValue: String): Id?
}

/**
 * [IdSerializer] intended for serialising/de-serialising enums.
 *
 * Enums are serialised by their [Enum.name]. When [de-serialising][deserialize],
 * the [enumEntries] are used.
 */
class EnumIdSerializer<E : Enum<E>>(
    val enumEntries: EnumEntries<E>
) : IdSerializer<E>() {
    override fun deserialize(rawValue: String): E? = enumEntries.find { it.name == rawValue }

    override fun serialize(id: E): String = id.name
}

/** Creates an [EnumIdSerializer] with the given reified [enum][E]. */
inline fun <reified E : Enum<E>> enumIdSerializer(
    entries: EnumEntries<E> = enumEntries<E>()
): EnumIdSerializer<E> = EnumIdSerializer(enumEntries = entries)

/** Default [IdSerializer] implementation which requires a [deserializer] to be specified. */
class DefaultIdSerializer<Id : Any>(
    val deserializer: (String) -> Id?,
    val serializer: (Id) -> String = { it.toString() }
) : IdSerializer<Id>() {
    override fun serialize(id: Id): String = serializer(id)
    override fun deserialize(rawValue: String): Id? = deserializer(rawValue)
}

/**
 * Creates an [IdSerializer] using functional syntax.
 * @param deserializer Function used to de-serialise the stored [String] ID to its [Id]
 * equivalent.
 * @param serializer Function used to serialise the [Id] to its [String] equivalent.
 */
fun <Id : Any> idSerializer(
    deserializer: (String) -> Id?,
    serializer: (Id) -> String = { it.toString() }
): IdSerializer<Id> = DefaultIdSerializer(
    serializer = serializer,
    deserializer = deserializer
)

/** Creates an [IdSerializer] intended for storing [integers][Int]. */
fun intIdSerializer(): IdSerializer<Int> = DefaultIdSerializer(
    deserializer = { it.toInt() }
)

/** Creates an [IdSerializer] intended for storing [longs][Long]. */
fun longIdSerializer(): IdSerializer<Long> = DefaultIdSerializer(
    deserializer = { it.toLong() }
)
