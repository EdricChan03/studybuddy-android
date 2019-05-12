package com.edricchan.studybuddy.utils

import android.content.Context
import java.io.File

class SharedPrefUtils(
		val context: Context
) {
	/**
	 * Checks if the shared preferences [fileName] exists
	 * @param fileName The name of the shared preference (without the file extension)
	 * @return [true] if the specified shared preference exists, [false] otherwise
	 */
	fun sharedPrefFileExists(fileName: String) = File("/data/data/${context.packageName}/shared_prefs/$fileName.xml").exists()

	/**
	 * Retrieve the shared preference file of the specified [fileName]
	 * @param fileName The file name of the shared preference (without the file extension)
	 * @return A [File] instance
	 */
	fun getSharedPrefsFile(fileName: String) = File("/data/data/${context.packageName}/shared_prefs/$fileName.xml")

	/**
	 * Retrieve the default shared preference file of the application
	 * @return A [File] instance
	 */
	fun getDefaultSharedPrefsFile() = File("/data/data/${context.packageName}/shared_prefs/${context.packageName}_preferences.xml")
	/**
	 * Retrieve the shared preferences file directory
	 * @return A [File] instance
	 */
	fun getSharedPrefsFileDir() = File("/data/data/${context.packageName}/shared_prefs")
}