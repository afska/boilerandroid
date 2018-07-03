package io.j3solutions.boilerandroid.controllers

import android.view.View
import io.j3solutions.boilerandroid.R
import io.j3solutions.boilerandroid.persistence.Db
import timber.log.Timber

class PostsController : BaseController(R.layout.controller_posts) {
	override fun onViewBound(view: View) {
		Db.instance { db ->
			db.blogPostDao().getPosts().subscribe {
				Timber.d("TENGO BLOGPOST DE LA DB $it")
			}
		}
	}
}