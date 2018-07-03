package io.j3solutions.boilerandroid.controllers

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import io.j3solutions.boilerandroid.R
import io.j3solutions.boilerandroid.persistence.Db
import io.j3solutions.boilerandroid.utils.subscribe
import io.j3solutions.boilerandroid.views.adapters.BlogPostsRecyclerAdapter
import kotlinx.android.synthetic.main.controller_blogposts.view.*
import timber.log.Timber

class BlogPostsController : BaseController(R.layout.controller_blogposts) {
	override fun onViewBound(view: View) {
		val adapter = BlogPostsRecyclerAdapter {
			Timber.d("CLICKED ${it.title.rendered}")
		}

		view.recycler.adapter = adapter
		view.recycler.layoutManager = LinearLayoutManager(activity)

		Db.instance { db ->
			db.blogPostDao().getPosts().subscribe(this) {
				adapter.populate(it)
			}
		}
	}
}