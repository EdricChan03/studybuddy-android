package com.edricchan.studybuddy.extensions.workmanager

import androidx.work.Data

/**
 * Returns a new [Data] with the given key/value pairs as elements.
 *
 * @throws IllegalArgumentException When a value is not a supported type of [Data].
 */
fun dataOf(vararg pairs: Pair<String, Any?>) = Data.Builder().apply {
	for ((key, value) in pairs) {
		when (value) {
			null -> putString(key, null) // Any nullable type will suffice.

			// Scalars
			is Boolean -> putBoolean(key, value)
			is Byte -> putByte(key, value)
			is Double -> putDouble(key, value)
			is Float -> putFloat(key, value)
			is Int -> putInt(key, value)
			is Long -> putLong(key, value)

			// Scalar arrays
			is BooleanArray -> putBooleanArray(key, value)
			is ByteArray -> putByteArray(key, value)
			is DoubleArray -> putDoubleArray(key, value)
			is FloatArray -> putFloatArray(key, value)
			is IntArray -> putIntArray(key, value)
			is LongArray -> putLongArray(key, value)

			// Reference arrays
			is Array<*> -> {
				val componentType = value::class.java.componentType!!
				@Suppress("UNCHECKED_CAST") // Checked by reflection.
				when {
					String::class.java.isAssignableFrom(componentType) -> {
						putStringArray(key, value as Array<String>)
					}
					else -> {
						val valueType = componentType.canonicalName
						throw IllegalArgumentException(
								"Illegal value array type $valueType for key \"$key\"")
					}
				}
			}
			else -> {
				val valueType = value.javaClass.canonicalName
				throw IllegalArgumentException("Illegal value type $valueType for key \"$key\"")
			}
		}
	}
}.build()