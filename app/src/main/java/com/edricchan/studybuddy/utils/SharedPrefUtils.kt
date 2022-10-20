package com.edricchan.studybuddy.utils

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.preference.PreferenceManager
import java.io.File
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.moveTo

@Deprecated("Use the appropriate extension functions instead")
class SharedPrefUtils(
    private val context: Context
) {
    /**
     * Checks if the shared preferences [fileName] exists.
     * @param fileName The name of the shared preference (without the file extension).
     * @param packageName The package's name to retrieve the shared preference from. Defaults to the
     * specified [context]'s package name (or [Context.getPackageName]).
     * @return `true` if the specified shared preference exists, `false` otherwise.
     */
    @Deprecated("Use the extension function on Context instead")
    fun sharedPrefFileExists(fileName: String, packageName: String = context.packageName) =
        File("/data/data/$packageName/shared_prefs/$fileName.xml").exists()

    /**
     * Retrieve the shared preference file of the specified [fileName]
     * @param fileName The file name of the shared preference (without the file extension)
     * @param packageName The package's name to retrieve the shared preference from. Defaults to the
     * specified [context]'s package name (or [Context.getPackageName]).
     * @return A [File] instance
     */
    @Deprecated("Use the extension function on Context instead")
    fun getSharedPrefsFile(fileName: String, packageName: String = context.packageName) =
        File("/data/data/$packageName/shared_prefs/$fileName.xml")
}

/**
 * Class used to represent the return value of any of the [SharedPrefUtils] extension functions.
 *
 * Internally, the methods on this class use the [java.nio] API on Android Oreo and higher, or
 * the [java.io] API on older versions.
 *
 * ## Usage
 * The appropriate methods defined in this value class should be used to retrieve the value as
 * a [File] ([asFile]) or as a [Path] ([asPath]).
 *
 * * To check if the file exists, use the [exists] method, which uses the
 * [Path.exists] extension method on Android Oreo and above, or the
 * [File.exists] method on older versions.
 * * To move the file to a new location, use the [moveTo] method, which uses the
 * [Path.moveTo] extension method on Android Oreo and above, or the
 * [File.copyTo] extension method on older versions.
 * @property filePath The internal file path of the shared preference file.
 */
@JvmInline
value class SharedPrefFile(private val filePath: String) {
    private fun supportsPath() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

    /** Retrieves the shared preference file as a [java.nio.file.Path]. */
    fun asPath() = Path(filePath)

    /** Retrieves the shared preference file as a [File]. */
    fun asFile() = File(filePath)

    /** Checks whether the shared preference file exists. */
    fun exists() =
        if (supportsPath()) asPath().exists()
        else asFile().exists()

    private fun Path.toSharedPrefFile() = SharedPrefFile(toString())
    private fun File.toSharedPrefFile() = SharedPrefFile(path)

    /**
     * Moves the shared preference file to the [target].
     * @param target The shared preference file to move to.
     * @param overwrite Whether to overwrite the [target] if it already exists.
     * @return The moved shared preference file.
     */
    fun moveTo(target: SharedPrefFile, overwrite: Boolean = false) =
        if (supportsPath()) asPath().moveTo(target.asPath(), overwrite).toSharedPrefFile()
        else asFile().run {
            val targetFile = copyTo(target.asFile(), overwrite)
            delete()
            targetFile
        }.toSharedPrefFile()
}

/** Creates an instance of [SharedPrefUtils] with the specified [Context]. */
@Deprecated(
    "Use the appropriate extension functions on Context instead to retrieve a shared " +
        "preference file, or to check if a shared preference file exists"
)
val Context.sharedPrefUtils
    get() = SharedPrefUtils(this)

/**
 * Checks if the shared preferences [fileName] exists.
 * @param fileName The name of the shared preference (without the file extension).
 * @param packageName The package's name to retrieve the shared preference from. Defaults to the
 * specified [Context]'s package name (or [Context.getPackageName]).
 * @return A [SharedPrefFile] instance.
 * (To retrieve the file as a [Path], use [SharedPrefFile.asPath]. To retrieve the file as a [File],
 * use [SharedPrefFile.asFile])
 */
fun Context.getSharedPreferencesFile(
    fileName: String, packageName: String = this.packageName
) = SharedPrefFile("/data/data/$packageName/shared_prefs/$fileName.xml")

/**
 * Retrieves the shared preference file from the specified [fileName].
 * @param fileName The file name of the shared preference (without the file extension)
 * @param packageName The package's name to retrieve the shared preference from. Defaults to the
 * specified [Context]'s package name (or [Context.getPackageName]).
 * @return `true` if the specified shared preference exists, `false` otherwise.
 */
fun Context.sharedPreferencesFileExists(
    fileName: String, packageName: String = this.packageName
) =
    getSharedPreferencesFile(fileName, packageName).exists()

/** Retrieves the default shared preference file. */
val Context.defaultSharedPreferences: SharedPreferences
    get() = PreferenceManager.getDefaultSharedPreferences(this)
