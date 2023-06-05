package com.edricchan.studybuddy.exts.androidx.preference

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.Discouraged
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.div
import kotlin.io.path.exists
import kotlin.io.path.moveTo
import kotlin.io.path.notExists
import kotlin.io.path.pathString

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
 * * To check if the file exists, use the [exists]/[notExists] methods, which uses the
 * [Path.exists] extension method on Android Oreo and above, or the
 * [File.exists] method on older versions.
 * * To move the file to a new location, use the [moveTo] method, which uses the
 * [Path.moveTo] extension method on Android Oreo and above, or the
 * [File.copyTo] extension method on older versions.
 * @property filePath The internal file path of the shared preference file.
 */
@Deprecated("No longer needed as java.nio.file.Path APIs can now be desugared")
@SuppressLint("DiscouragedApi")
@JvmInline
value class SharedPrefFile(private val filePath: String) {
    @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O)
    private val supportsPath get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

    /** Retrieves the shared preference file as a [java.nio.file.Path]. */
    fun asPath() = Path(filePath)

    /** Retrieves the shared preference file as a [File]. */
    @Discouraged("java.nio.file.Path APIs can now be desugared - use asPath() where preferable")
    fun asFile() = File(filePath)

    /** Checks whether the shared preference file exists. */
    @Deprecated(
        "Use asPath().exists()",
        ReplaceWith(
            "asPath().exists()",
            "kotlin.io.path.exists"
        )
    )
    fun exists() =
        if (supportsPath) asPath().exists()
        else asFile().exists()

    /** Checks whether the shared preference file does not exist. */
    @Deprecated(
        "Use asPath().notExists()",
        ReplaceWith(
            "asPath().notExists()",
            "kotlin.io.path.notExists"
        )
    )
    fun notExists() =
        if (supportsPath) asPath().notExists()
        else !asFile().exists()

    /**
     * Moves the shared preference file to the [target].
     * @param target The shared preference file to move to.
     * @param overwrite Whether to overwrite the [target] if it already exists.
     * @param onDelete Function to be invoked when the file is deleted. **Warning: This has no
     * effect when used on Android Oreo and up.**
     * @return The moved shared preference file.
     */
    @Deprecated(
        "Use asPath().moveTo(...)",
        ReplaceWith(
            "asPath().moveTo(target.asPath(), overwrite)",
            "kotlin.io.path.moveTo"
        )
    )
    suspend fun moveTo(
        target: SharedPrefFile,
        overwrite: Boolean = false
    ) = withContext(Dispatchers.IO) {
        async {
            if (supportsPath) asPath().moveTo(target.asPath(), overwrite).toSharedPrefFile()
            else asFile().run {
                copyTo(target.asFile())
                delete()
                target
            }
        }
    }
}

private fun Path.toSharedPrefFile() = SharedPrefFile(pathString)
private fun File.toSharedPrefFile() = SharedPrefFile(path)

private fun SharedPrefFile(file: File) = file.toSharedPrefFile()
private fun SharedPrefFile(path: Path) = path.toSharedPrefFile()

private val Context.dataDirCompat
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        dataDir.toPath()
    } else {
        Path("/data/data")
    }

/**
 * Retrieves the shared preferences file given the [fileName] as a [SharedPrefFile].
 * @param fileName The name of the shared preference (without the file extension).
 * @return A [SharedPrefFile] instance.
 * (To retrieve the file as a [Path], use [SharedPrefFile.asPath]. To retrieve the file as a [File],
 * use [SharedPrefFile.asFile])
 */
@Deprecated(
    "Use getSharedPreferencesPath instead. " +
        "Note that the method returns a java.nio.file.Path instead of a custom value class",
    ReplaceWith(
        "getSharedPreferencesPath(fileName)",
        "com.edricchan.studybuddy.exts.androidx.preference.getSharedPreferencesPath"
    )
)
fun Context.getSharedPreferencesFile(
    fileName: String
) = SharedPrefFile(
    dataDirCompat.resolve("shared_prefs").resolve("$fileName.xml")
)

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

/** Retrieves the default shared preference file. */
@Deprecated(
    "Use defaultSharedPreferencesPath instead. " +
        "Note that the getter returns a java.nio.file.Path instead of a custom value class",
    ReplaceWith(
        "defaultSharedPreferencesPath",
        "com.edricchan.studybuddy.exts.androidx.preference.defaultSharedPreferencesPath"
    )
)
val Context.defaultSharedPreferencesFile
    get() = getSharedPreferencesFile(defaultSharedPreferencesName)

/** Retrieves the default shared preference file as a [Path]. */
val Context.defaultSharedPreferencesPath
    get() = getSharedPreferencesPath(defaultSharedPreferencesName)

/** Whether the default shared preference file exists. */
val Context.defaultSharedPreferencesFileExists
    get() = defaultSharedPreferencesPath.exists()
