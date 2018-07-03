package io.j3solutions.boilerandroid.models

import android.arch.persistence.room.*
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

	@Embedded(prefix = "title")
	var title: Title = Title()

	@Embedded(prefix = "content")
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