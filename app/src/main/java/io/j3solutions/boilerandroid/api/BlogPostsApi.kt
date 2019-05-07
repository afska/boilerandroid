package io.j3solutions.boilerandroid.api

import io.j3solutions.boilerandroid.models.BlogPost
import io.j3solutions.boilerandroid.utils.api.newApi
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

interface BlogPostsApi {
	@GET("boilerandroid.json")
	fun getAll(): Single<Response<List<BlogPost>>>

	companion object {
		val api: BlogPostsApi get() = newApi("https://gist.githubusercontent.com/rodri042/6e2d1ac8c8a9ecfa6cd10bbed4ac9389/raw/dd49dd24ab7f03fc1da2edc330dc34b54722f693/")
	}
}
