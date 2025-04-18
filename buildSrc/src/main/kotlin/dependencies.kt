@file:Suppress("ClassName", "HardCodedStringLiteral", "LongLine")

import java.io.File

object deps {
    object build {
        object deepLink {
            private const val outputSuffix = "docs/deeplinks.md"
            fun getDocOutput(dir: File): String {
                // Interestingly, the `toString` method of `java.io.File` actually invokes
                // the `getPath` method so we don't have to convert it to a path explicitly.
                return "$dir/$outputSuffix"
            }
        }
    }
}
