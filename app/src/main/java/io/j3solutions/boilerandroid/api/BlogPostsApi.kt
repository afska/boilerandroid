package io.j3solutions.boilerandroid.api

import io.j3solutions.boilerandroid.BuildConfig
import io.j3solutions.boilerandroid.models.BlogPost
import io.j3solutions.boilerandroid.utils.api.newApi
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET

interface BlogPostsApi {
    @GET("boilerandroid.json")
    fun getAll(): Single<Response<List<BlogPost>>>

    companion object {
        val api: BlogPostsApi get() = newApi(BuildConfig.BASE_API_URL)
    }
}
