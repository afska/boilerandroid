package io.j3solutions.boilerandroid.persistence

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import io.j3solutions.boilerandroid.RootApplication
import io.j3solutions.boilerandroid.models.BlogPost
import io.j3solutions.boilerandroid.models.BlogPostDao
import io.j3solutions.boilerandroid.utils.newSingle

@Database(
	entities = arrayOf(
		BlogPost::class
	),
	version = 2
)
@TypeConverters(Converters::class)
abstract class Db : RoomDatabase() {
	abstract fun blogPostDao(): BlogPostDao

	companion object {
		private var _instance: Db? = null

		val instance: Db
			@Synchronized get() {
				if (_instance == null) {
					_instance = Room
						.databaseBuilder(RootApplication.appContext,
							Db::class.java, "boilerandroid-database")
						.build()
				}
				return _instance!!
			}
	}

	operator fun <T> invoke(action: (Db) -> (T)) {
		newSingle { action(instance) }.subscribe({}, {})
	}
}
