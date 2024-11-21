package com.edricchan.studybuddy.exts.androidx.preference

import android.content.Context
import android.os.Build
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.div
import kotlin.io.path.exists

private val Context.dataDirCompat
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        dataDir.toPath()
    } else {
        Path(applicationInfo.dataDir)
    }

/**
 * Retrieves the shared preferences file given the [fileName] as a [Path].
 * @param fileName The name of the shared preference (without the file extension).
 * @return A [Path] to the file.
 */
fun Context.getSharedPreferencesPath(fileName: String) =
    dataDirCompat / "shared_prefs" / "$fileName.xml"

/**
 * Checks if the shared preferences file with the given [fileName] exists.
 * @param fileName The file name of the shared preference (without the file extension)
 * @return `true` if the specified shared preference exists, `false` otherwise.
 */
fun Context.sharedPreferencesFileExists(
    fileName: String
) = getSharedPreferencesPath(fileName).exists()

/** Retrieves the default shared preference name. */
// Implementation based from androidx.preference.PreferenceManager
val Context.defaultSharedPreferencesName get() = "${packageName}_preferences"

/** Retrieves the default shared preference file as a [Path]. */
val Context.defaultSharedPreferencesPath
    get() = getSharedPreferencesPath(defaultSharedPreferencesName)

/** Whether the default shared preference file exists. */
val Context.defaultSharedPreferencesFileExists
    get() = defaultSharedPreferencesPath.exists()
