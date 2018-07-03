package io.j3solutions.boilerandroid.api

import io.j3solutions.boilerandroid.models.BlogPost
import io.j3solutions.boilerandroid.utils.api.newApi
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

interface BlogPostsApi {
	@GET("posts")
	fun getAll(): Single<Response<List<BlogPost>>>

	companion object {
		val api: BlogPostsApi get() = newApi("https://boilerandroid.000webhostapp.com/wp-json/wp/v2/")
	}
}
