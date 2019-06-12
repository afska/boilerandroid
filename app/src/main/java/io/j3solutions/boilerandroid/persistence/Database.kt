package io.j3solutions.boilerandroid.persistence

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.j3solutions.boilerandroid.RootApplication
import io.j3solutions.boilerandroid.models.BlogPost
import io.j3solutions.boilerandroid.models.BlogPostDao
import io.j3solutions.boilerandroid.utils.extensions.newSingle
import io.reactivex.Single
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.disposables.Disposable
import androidx.room.Database as DatabaseAnnotation

@DatabaseAnnotation(
    entities = [BlogPost::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {
    abstract fun blogPostDao(): BlogPostDao

    companion object {
        private var _instance: Database? = null

        val instance: Database
            @Synchronized get() {
                if (_instance == null) {
                    _instance = Room
                        .databaseBuilder(
                            RootApplication.appContext,
                            Database::class.java,
                            "boilerandroid-database"
                        )
                        .build()
                }
                return _instance!!
            }
    }

    fun <T> doSingle(action: (Database) -> (T)): Single<T> {
        return newSingle { action(instance) }
    }

    @CheckReturnValue
    operator fun <T> invoke(action: (Database) -> (T)): Disposable {
        return doSingle(action).subscribe({}, {})
    }
}
