package com.edricchan.studybuddy.utils

import android.content.Context
import java.io.File

class SharedPrefUtils(
    val context: Context
) {
    /**
     * Checks if the shared preferences [fileName] exists.
     * @param fileName The name of the shared preference (without the file extension).
     * @param packageName The package's name to retrieve the shared preference from. Defaults to the
     * specified [context]'s package name (or [Context.getPackageName]).
     * @return `true` if the specified shared preference exists, `false` otherwise.
     */
    fun sharedPrefFileExists(fileName: String, packageName: String = context.packageName) =
        File("/data/data/$packageName/shared_prefs/$fileName.xml").exists()

    /**
     * Retrieve the shared preference file of the specified [fileName]
     * @param fileName The file name of the shared preference (without the file extension)
     * @param packageName The package's name to retrieve the shared preference from. Defaults to the
     * specified [context]'s package name (or [Context.getPackageName]).
     * @return A [File] instance
     */
    fun getSharedPrefsFile(fileName: String, packageName: String = context.packageName) =
        File("/data/data/$packageName/shared_prefs/$fileName.xml")
}