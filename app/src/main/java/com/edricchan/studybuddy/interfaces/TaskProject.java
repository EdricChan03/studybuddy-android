package com.edricchan.studybuddy.interfaces;

import android.graphics.Color;

import androidx.annotation.ColorRes;

public class TaskProject {
	/**
	 * Specifies the color (as a hexadecimal color) of the project
	 */
	private String color;
	/**
	 * Specifies the document ID of the project
	 */
	private String id;
	/**
	 * Specifies the name of the project
	 */
	private String name;

	// Default constructor for Cloud Firestore
	public TaskProject() {
	}


	/**
	 * Retrieves the color of the project
	 *
	 * @return The color (as a hexdecimal color) of the project
	 */
	public String getColor() {
		return this.color;
	}

	/**
	 * Retrieves the project's document ID
	 *
	 * @return The project's document ID
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Retrieves the name of the project
	 *
	 * @return The name of the project
	 */
	public String getName() {
		return this.name;
	}

	public static class Builder {
		private TaskProject project;

		/**
		 * Checks if an RGB code is valid
		 *
		 * @param code The code to check
		 * @return True if it is valid, false otherwise
		 */
		private boolean checkValidRGBCode(int code) {
			return code >= 0 && code <= 255;
		}

		/**
		 * Converts a color from a resource to a hexdecimal color
		 *
		 * @param color The color to convert
		 * @return The color in hexadecimal form
		 */
		private String convertColorToHex(@ColorRes int color) {
			return String.format("#%06X", (0xFFFFFF & color));
		}

		/**
		 * Converts a RGB color to a hexdecimal color
		 *
		 * @param r The red value
		 * @param g The green value
		 * @param b The blue value
		 * @return The color in hexadecimal form
		 */
		private String convertRGBtoHex(int r, int g, int b) {
			return String.format("#%02x%02x%02x", r, g, b);
		}

		/**
		 * Creates a builder for a new project
		 */
		public Builder() {
			this.project = new TaskProject();
		}

		/**
		 * Creates a builder, but allows for a predefined project to be specified
		 *
		 * @param project The predefined project
		 */
		public Builder(TaskProject project) {
			this.project = project;
		}

		/**
		 * Sets the color of this project
		 *
		 * @param color A hexadecimal color
		 * @return The builder object to allow for chaining of methods
		 */
		public Builder setColor(String color) {
			try {
				Color.parseColor(color);
				// The color is valid
				project.color = color;
			} catch (IllegalArgumentException iae) {
				// This color string is not valid
				throw new IllegalArgumentException("Please supply a valid hexadecimal color!");
			}
			return this;
		}

		/**
		 * Sets the color of this project
		 *
		 * @param r The red value
		 * @param g The green value
		 * @param b The blue value
		 * @return The builder object to allow for chaining of methods
		 */
		public Builder setColor(int r, int g, int b) {
			if (checkValidRGBCode(r)) {
				if (checkValidRGBCode(g)) {
					if (checkValidRGBCode(b)) {
						project.color = this.convertRGBtoHex(r, g, b);
					} else {
						throw new IllegalArgumentException("Please supply a valid RGB blue code!");
					}
				} else {
					throw new IllegalArgumentException("Please supply a valid RGB green code!");
				}
			} else {
				throw new IllegalArgumentException("Please supply a valid RGB red code!");
			}
			return this;
		}

		/**
		 * Sets the color of this project
		 *
		 * @param color A color resource
		 * @return The builder object to allow for chaining of methods
		 */
		public Builder setColor(@ColorRes int color) {
			project.color = this.convertColorToHex(color);
			return this;
		}

		/**
		 * Sets the ID of this project
		 *
		 * @param id This project's document ID
		 * @return The builder object to allow for chaining of methods
		 * @deprecated The document ID is automatically appended to the task once it is added to the database
		 */
		@Deprecated
		public Builder setId(String id) {
			project.id = id;
			return this;
		}

		/**
		 * Sets the name of this project
		 *
		 * @param name The name of this project
		 * @return The builder object to allow for chaining of methods
		 */
		public Builder setName(String name) {
			project.name = name;
			return this;
		}

		/**
		 * Checks if all values in this project are valid and returns the project
		 *
		 * @return The created project
		 */
		public TaskProject create() {
			if (project.color.isEmpty()) {
				// Use the default color
				project.color = "#F5F5F5";
			}

			// Finally, return the project
			return project;
		}
	}
}
