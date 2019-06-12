package io.j3solutions.boilerandroid.models

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import io.reactivex.Flowable

@Entity(tableName = BlogPost.TABLE_NAME)
class BlogPost {
    companion object {
        const val TABLE_NAME = "BlogPosts"
    }

    @PrimaryKey
    var id: Int = 0

    var date: String = ""
    var date_gmt: String = ""
    var slug: String = ""

    @Embedded(prefix = "title_")
    var title: Title = Title()

    @Embedded(prefix = "content_")
    var content: Content = Content()
}

class Content {
    var rendered: String = ""
    var protected: Boolean = false
}

class Title {
    var rendered: String = ""
}

@Dao
interface BlogPostDao {
    @Query("SELECT * FROM ${BlogPost.TABLE_NAME}")
    fun getPosts(): Flowable<List<BlogPost>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(blogPosts: List<BlogPost>)
}