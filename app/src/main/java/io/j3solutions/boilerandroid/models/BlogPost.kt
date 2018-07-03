package io.j3solutions.boilerandroid.models

import android.arch.persistence.room.*
import io.reactivex.Flowable

@Entity(tableName = BlogPost.TABLE_NAME)
data class BlogPost(
	@PrimaryKey val id: Int,
	val date: String,
	val date_gmt: String,
	val slug: String
//	@Embedded val title: Title,
//	@Embedded val content: Content
) {
	companion object {
		const val TABLE_NAME = "BlogPosts"
	}

	@Ignore
	constructor() : this(0, "", "", "")
}

//data class Content(
//    val rendered: String = "",
//    val protected: Boolean = false
//)
//
//data class Title(
//    val rendered: String = ""
//)

@Dao
interface BlogPostDao {
	@Query("SELECT * FROM ${BlogPost.TABLE_NAME}")
	fun getPosts(): Flowable<BlogPost>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertAll(blogPosts: List<BlogPost>)
}