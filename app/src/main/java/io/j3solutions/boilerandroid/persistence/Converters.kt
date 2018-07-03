package io.j3solutions.boilerandroid.persistence

import android.arch.persistence.room.TypeConverter
import java.util.*

class Converters {
	@TypeConverter
	fun fromTimestamp(value: Long): Date {
		return Date(value)
	}

	@TypeConverter
	fun dateToTimestamp(date: Date): Long {
		return date.time
	}
}