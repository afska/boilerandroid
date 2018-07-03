package io.j3solutions.boilerandroid.controllers

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import io.j3solutions.boilerandroid.R
import io.j3solutions.boilerandroid.models.BlogPost
import io.j3solutions.boilerandroid.views.adapters.SimpleRecyclerAdapter
import kotlinx.android.synthetic.main.controller_blogposts.view.*
import kotlinx.android.synthetic.main.item_blogpost.view.*

class BlogPostsController : BaseController(R.layout.controller_blogposts) {
	override fun onViewBound(view: View) {
		val adapter = SimpleRecyclerAdapter<BlogPost>(R.layout.item_blogpost, { view, blogPost, adapter ->
			view.title.text = blogPost.title.rendered
			view.content.text = blogPost.content.rendered
		})

		view.recycler.adapter = adapter
		view.recycler.layoutManager = LinearLayoutManager(activity)

		adapter.populate(this) { it.blogPostDao().getPosts() }
	}
}