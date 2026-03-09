package com.edricchan.studybuddy.exts.protobuf.temporal

import com.google.protobuf.duration
import java.time.Duration
// We use DurationP instead of DurationProto as there's also a class with the same name
// in the package
import com.google.protobuf.Duration as DurationP

fun DurationP.toJavaDuration(): Duration = Duration.ofSeconds(
    seconds, nanos.toLong()
)

fun Duration.toProtoDuration(): DurationP = duration {
    seconds = this@toProtoDuration.seconds
    nanos = this@toProtoDuration.nano
}
