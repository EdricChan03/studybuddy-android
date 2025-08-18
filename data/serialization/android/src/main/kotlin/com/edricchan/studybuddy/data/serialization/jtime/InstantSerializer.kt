package com.edricchan.studybuddy.data.serialization.jtime

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant

/**
 * [KSerializer] for [Instant]s. The result of [Instant.toString] is used
 * when encoding the value.
 */
object InstantSerializer : KSerializer<Instant> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(
            "com.edricchan.studybuddy.data.serialization.jtime.InstantSerializer",
            PrimitiveKind.STRING
        )

    override fun serialize(encoder: Encoder, value: Instant) =
        encoder.encodeString(value.toString())

    @RequiresApi(Build.VERSION_CODES.O)
    override fun deserialize(decoder: Decoder): Instant = Instant.parse(decoder.decodeString())
}
