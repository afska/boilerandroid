package io.j3solutions.boilerandroid.preferences

import android.content.Context
import android.content.SharedPreferences
import io.j3solutions.boilerandroid.RootApplication
import java.lang.Exception
import java.util.*
import kotlin.reflect.KProperty

object PreferencesManager {
	val APP_PREFERENCES: String = "APP_PREFERENCES"
	val preferences: SharedPreferences by lazy { getSharedPreferences(RootApplication.appContext) }

	fun getSharedPreferences(context: Context): SharedPreferences {
		return context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
	}

	fun saveInt(label: String, number: Int) {
		preferences.edit().putInt(label, number).apply()
	}

	fun getInt(label: String): Int {
		return preferences.getInt(label, -1)
	}

	fun delete(label: String) {
		preferences.edit().remove(label).apply()
	}

	fun save(label: String, elem: Any) {
		when (elem) {
			is Int -> preferences.edit().putInt(label, elem).apply()
			is String -> preferences.edit().putString(label, elem).apply()
			is Float -> preferences.edit().putFloat(label, elem).apply()
			is Boolean -> preferences.edit().putBoolean(label, elem).apply()
			is Date -> preferences.edit().putLong(label, elem.time).apply()
			else -> throw DelegatedPreference.TypeNotImplementedException(label)
		}
	}

	@Suppress("UNCHECKED_CAST", "IMPLICIT_CAST_TO_ANY")
	fun <T> get(label: String, default: T): T =
		when (default) {
			is Int -> preferences.getInt(label, default)
			is String -> preferences.getString(label, default)
			is Float -> preferences.getFloat(label, default)
			is Boolean -> preferences.getBoolean(label, default)
			is Date -> Date(preferences.getLong(label, default.time))
			else -> throw DelegatedPreference.TypeNotImplementedException(label)
		} as T

	fun increment(s: String, q: Int = 1): Int {
		val n = get(s, 0)
		save(s, n + q)
		return n + q
	}
}

class DelegatedPreference<out T>(val default: T, val prefix: String = "") {
	@Suppress("UNCHECKED_CAST")
	operator fun getValue(thisRef: Any?, prop: KProperty<*>): T {
		return PreferencesManager.get(prefix + prop.name, default)
	}

	operator fun setValue(thisRef: Any?, prop: KProperty<*>, value: Any) {
		PreferencesManager.save(prefix + prop.name, value)
	}

	class TypeNotImplementedException(propName: String) : Exception("Type of $propName is not implemented on DelegatedPreference and thus invalid")
}
