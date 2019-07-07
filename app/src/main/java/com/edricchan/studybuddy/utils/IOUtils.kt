package com.edricchan.studybuddy.utils

import java.io.File

/**
 * Utility class for I/O-related operations
 */
class IOUtils {
	companion object {

		/**
		 * Converts a path name to a [File] object
		 * @param pathName the path name
		 * @return A [File] object
		 */
		fun toFileObj(pathName: String) = File(pathName)

		/**
		 * Checks if a file exists
		 * @param pathName The full path to the file
		 * @return [true] if the file exists, [false] if it does not
		 * @see File.exists
		 */
		fun fileExists(pathName: String): Boolean = File(pathName).exists()

		/**
		 * Checks if a file does not exist
		 * @param pathName The full path to the file
		 * @return [true] if the file does not exist, [false] if it does
		 * @see File.exists
		 */
		fun fileDoesNotExist(pathName: String): Boolean = !fileExists(pathName)

		/**
		 * Checks if a file exists and is a directory
		 * Note: This function also checks if the file exists before checking if the file is a directory
		 * @param pathName The full path to the directory
		 * @return [true] if the file exists and is a directory, [false] otherwise
		 * @see File.isDirectory
		 */
		fun isFileDirectory(pathName: String): Boolean = fileExists(pathName) && File(pathName).isDirectory

		/**
		 * Checks if a file exists and is a directory
		 * Note: This is an alias for [isFileDirectory]
		 * Note: This function also checks if the file exists before checking if the file is a directory
		 * @param pathName The full path to the directory
		 * @return [true] if the file exists and is a directory, [false] otherwise
		 * @see File.isDirectory
		 */
		fun isFileFolder(pathName: String): Boolean = isFileDirectory(pathName)

		/**
		 * Checks if a file exists and is actually a file
		 * Note: This function also checks if the file exists before checking if the file is actually a file and not a directory
		 * @param pathName The full path to the file
		 * @return [true] if the file exists and is not a directory, [false] otherwise
		 * @see File.isDirectory
		 */
		fun isFileNotDirectory(pathName: String): Boolean = fileExists(pathName) && !File(pathName).isDirectory

		/**
		 * Check if the file exists and deletes the file specified
		 * @param pathName The full path to the file
		 * @return [true] if the file exists and is deleted, [false] otherwise
		 * @see File.delete
		 */
		fun deleteFile(pathName: String): Boolean = fileExists(pathName) && File(pathName).delete()

		/**
		 * Recursively deletes files/directories in a directory
		 * @param fileOrDirectory The file/directory to delete
		 */
		fun deleteRecursive(fileOrDirectory: File) {
			if (fileOrDirectory.isDirectory)
				for (child in fileOrDirectory.listFiles())
					deleteRecursive(child)
			fileOrDirectory.delete()
		}
	}
}