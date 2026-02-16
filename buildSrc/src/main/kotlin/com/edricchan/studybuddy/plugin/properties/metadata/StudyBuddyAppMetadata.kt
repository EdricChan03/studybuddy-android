package com.edricchan.studybuddy.plugin.properties.metadata

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import java.time.Instant

abstract class StudyBuddyAppMetadata : ExtensionAware {
    /**
     * The current Git commit in its SHA form.
     * This value can be retrieved from entering `git rev-parse HEAD` in the command-line.
     */
    @get:Input
    abstract val gitCommitSha: Property<String>

    /**
     * The time when this build was requested in milli-seconds.
     *
     * Defaults to the [epoch milli-second value][Instant.toEpochMilli] of
     * [Instant.now] if not specified.
     */
    @get:Input
    // Instants are not supported in Configuration Cache, see
    // https://github.com/gradle/gradle/issues/13588
    abstract val buildTimeMillis: Property<Long>

    fun buildTime(time: Instant) {
        buildTimeMillis.set(time.toEpochMilli())
    }

    /**
     * The time when this build was requested as an [Instant].
     *
     * Specify this value via [buildTimeMillis] or [buildTime].
     */
    @get:Internal
    val buildInstant: Provider<Instant> get() = buildTimeMillis.map(Instant::ofEpochMilli)

    internal fun asJson() = AppMetadataJson(
        gitCommitSha = gitCommitSha.get(),
        buildTime = buildInstant.get()
    )

    @Serializable
    internal data class AppMetadataJson(
        val gitCommitSha: String,
        @Serializable(with = InstantSerializer::class) val buildTime: Instant
    ) {
        private object InstantSerializer : KSerializer<Instant> {
            override val descriptor: SerialDescriptor =
                PrimitiveSerialDescriptor("java.time.Instant", PrimitiveKind.STRING)

            override fun serialize(encoder: Encoder, value: Instant) =
                encoder.encodeString(value.toString())

            override fun deserialize(decoder: Decoder): Instant =
                Instant.parse(decoder.decodeString())
        }
    }
}
