package com.edricchan.studybuddy.utils.wire.datastore

import androidx.datastore.core.Serializer
import com.squareup.wire.ProtoAdapter
import java.io.InputStream
import java.io.OutputStream

/**
 * [Serializer] that uses the [adapter] as the source for [Serializer.readFrom]
 * and [Serializer.writeTo].
 * @see asSerializer
 */
open class WireSerializer<T>(
    private val adapter: ProtoAdapter<T>,
    override val defaultValue: T
) : Serializer<T> {
    override suspend fun readFrom(input: InputStream): T = adapter.decode(input)
    override suspend fun writeTo(t: T, output: OutputStream) {
        adapter.encode(output, t)
    }
}

/**
 * Returns a [Serializer] that uses the receiver [ProtoAdapter] as the source for
 * [Serializer.readFrom] and [Serializer.writeTo].
 */
inline fun <T> ProtoAdapter<T>.asSerializer(
    defaultValue: T
): WireSerializer<T> = WireSerializer<T>(
    this,
    defaultValue
)
