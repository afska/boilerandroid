package io.j3solutions.boilerandroid.controllers

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import io.j3solutions.boilerandroid.R
import io.j3solutions.boilerandroid.models.BlogPost
import io.j3solutions.boilerandroid.views.adapters.SimpleRecyclerAdapter
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.controller_blogposts.view.*
import kotlinx.android.synthetic.main.item_blogpost.view.*

class BlogPostsController : BaseController(R.layout.controller_blogposts) {
    override fun onViewBound(view: View) {
        val adapter =
            SimpleRecyclerAdapter<BlogPost>(R.layout.item_blogpost, { itemView, blogPost, _ ->
                itemView.title.text = blogPost.title.rendered
                itemView.content.text = blogPost.content.rendered
            })

        view.recycler.adapter = adapter
        view.recycler.layoutManager = LinearLayoutManager(activity)

        disposables += adapter.populate { it.blogPostDao().getPosts() }
    }
}