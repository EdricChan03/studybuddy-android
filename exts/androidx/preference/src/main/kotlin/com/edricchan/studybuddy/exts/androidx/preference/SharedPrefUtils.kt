package com.edricchan.studybuddy.exts.androidx.preference

import android.content.Context
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import java.io.File
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.moveTo

/**
 * Class used to represent the return value of [getSharedPreferencesFile] and
 * [sharedPreferencesFileExists].
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
    @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O)
    private val supportsPath get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

    /** Retrieves the shared preference file as a [java.nio.file.Path]. */
    fun asPath() = Path(filePath)

    /** Retrieves the shared preference file as a [File]. */
    fun asFile() = File(filePath)

    /** Checks whether the shared preference file exists. */
    fun exists() =
        if (supportsPath) asPath().exists()
        else asFile().exists()

    private fun Path.toSharedPrefFile() = SharedPrefFile(toString())
    private fun File.toSharedPrefFile() = SharedPrefFile(path)

    /**
     * Moves the shared preference file to the [target].
     * @param target The shared preference file to move to.
     * @param overwrite Whether to overwrite the [target] if it already exists.
     * @param onDelete Function to be invoked when the file is deleted. **Warning: This has no
     * effect when used on Android Oreo and up.**
     * @return The moved shared preference file.
     */
    fun moveTo(
        target: SharedPrefFile,
        overwrite: Boolean = false,
        onDelete: (Boolean) -> Unit = {}
    ) =
        if (supportsPath) asPath().moveTo(target.asPath(), overwrite).toSharedPrefFile()
        else asFile().run {
            val targetFile = copyTo(target.asFile(), overwrite)
            onDelete(delete())
            targetFile
        }.toSharedPrefFile()
}

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
