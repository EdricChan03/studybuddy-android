@file:Suppress("ClassName", "HardCodedStringLiteral", "LongLine")

import java.io.File

object deps {
    object build {
        object deepLink {
            const val customAnnotations = "com.edricchan.studybuddy.annotations.AppDeepLink," +
                    "com.edricchan.studybuddy.annotations.WebDeepLink"
            const val incremental = "true"
            private const val outputSuffix = "docs/deeplinks.md"
            fun getDocOutput(dir: File): String {
                // Interestingly, the `toString` method of `java.io.File` actually invokes
                // the `getPath` method so we don't have to convert it to a path explicitly.
                return "$dir/$outputSuffix"
            }
        }
        object repositories {
            const val jitpack = "https://jitpack.io"
        }
    }
}
