package com.edricchan.studybuddy.interfaces

import android.graphics.Color
import android.text.TextUtils

import androidx.annotation.ColorRes

// Default constructor for Cloud Firestore
data class TaskProject(
		/**
		 * Specifies the color/colour (as a hexadecimal color/colour) of the project
		 */
		var color: String? = null,
		/**
		 * Specifies the document ID of the project
		 */
		var id: String? = null,
		/**
		 * Specifies the name of the project
		 */
		var name: String? = null
) {
	class Builder {
		private var project: TaskProject? = null

		/**
		 * Checks if an RGB code is valid
		 *
		 * @param code The code to check
		 * @return True if it is valid, false otherwise
		 */
		private fun checkValidRGBCode(code: Int): Boolean {
			return code >= 0 && code <= 255
		}

		/**
		 * Converts a color from a resource to a hexdecimal color
		 *
		 * @param color The color to convert
		 * @return The color in hexadecimal form
		 */
		private fun convertColorToHex(@ColorRes color: Int): String {
			return String.format("#%06X", 0xFFFFFF and color)
		}

		/**
		 * Converts a RGB color to a hexdecimal color
		 *
		 * @param r The red value
		 * @param g The green value
		 * @param b The blue value
		 * @return The color in hexadecimal form
		 */
		private fun convertRGBtoHex(r: Int, g: Int, b: Int): String {
			return String.format("#%02x%02x%02x", r, g, b)
		}

		/**
		 * Creates a builder for a new project
		 */
		constructor() {
			this.project = TaskProject()
		}

		/**
		 * Creates a builder, but allows for a predefined project to be specified
		 *
		 * @param project The predefined project
		 */
		constructor(project: TaskProject) {
			this.project = project
		}

		/**
		 * Sets the color of this project
		 *
		 * @param color A hexadecimal color
		 * @return The builder object to allow for chaining of methods
		 */
		fun setColor(color: String): Builder {
			try {
				Color.parseColor(color)
				// The color is valid
				project!!.color = color
			} catch (iae: IllegalArgumentException) {
				// This color string is not valid
				throw IllegalArgumentException("Please supply a valid hexadecimal color!")
			}

			return this
		}

		/**
		 * Sets the color of this project
		 *
		 * @param r The red value
		 * @param g The green value
		 * @param b The blue value
		 * @return The builder object to allow for chaining of methods
		 */
		fun setColor(r: Int, g: Int, b: Int): Builder {
			if (checkValidRGBCode(r)) {
				if (checkValidRGBCode(g)) {
					if (checkValidRGBCode(b)) {
						project!!.color = this.convertRGBtoHex(r, g, b)
					} else {
						throw IllegalArgumentException("Please supply a valid RGB blue code!")
					}
				} else {
					throw IllegalArgumentException("Please supply a valid RGB green code!")
				}
			} else {
				throw IllegalArgumentException("Please supply a valid RGB red code!")
			}
			return this
		}

		/**
		 * Sets the color of this project
		 *
		 * @param color A color resource
		 * @return The builder object to allow for chaining of methods
		 */
		fun setColor(@ColorRes color: Int): Builder {
			project!!.color = this.convertColorToHex(color)
			return this
		}

		/**
		 * Sets the ID of this project
		 *
		 * @param id This project's document ID
		 * @return The builder object to allow for chaining of methods
		 */
		@Deprecated("The document ID is automatically appended to the task once it is added to the database")
		fun setId(id: String): Builder {
			project!!.id = id
			return this
		}

		/**
		 * Sets the name of this project
		 *
		 * @param name The name of this project
		 * @return The builder object to allow for chaining of methods
		 */
		fun setName(name: String): Builder {
			project!!.name = name
			return this
		}

		/**
		 * Checks if all values in this project are valid and returns the project
		 *
		 * @return The created project
		 */
		fun create(): TaskProject {
			if (TextUtils.isEmpty(project!!.color)) {
				// Use the default color
				project!!.color = "#F5F5F5"
			}

			// Finally, return the project
			return project as TaskProject
		}
	}
}
