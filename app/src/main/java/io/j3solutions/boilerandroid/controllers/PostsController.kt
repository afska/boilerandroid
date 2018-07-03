package io.j3solutions.boilerandroid.controllers

import android.view.View
import io.j3solutions.boilerandroid.R
import io.j3solutions.boilerandroid.api.PostsApi
import io.j3solutions.boilerandroid.utils.subscribe
import timber.log.Timber

class PostsController : BaseController(R.layout.controller_posts) {
	override fun onViewBound(view: View) {
		http(
			PostsApi.api.getAll()
		).subscribe(this) {
			Timber.d("LOS POSTS SON $it")
		}
	}
}