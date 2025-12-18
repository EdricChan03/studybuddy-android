/** Convention script for all modules. */
package com.edricchan.studybuddy.library

// TODO: Remove when Dagger bumps Kotlin metadata dependency to 2.3.0
//  (see also https://github.com/google/dagger/issues/5059)
configurations.configureEach {
    resolutionStrategy.force("org.jetbrains.kotlin:kotlin-metadata-jvm:2.3.0")
}
