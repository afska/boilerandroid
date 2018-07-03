package io.j3solutions.boilerandroid.views.adapters

import android.view.View
import io.j3solutions.boilerandroid.R
import io.j3solutions.boilerandroid.models.BlogPost
import kotlinx.android.synthetic.main.item_blogpost.view.*

class BlogPostsRecyclerAdapter(
	private val onBlogPostClick: (BlogPost) -> (Unit)
) : RecyclerAdapter<Any, BlogPostsRecyclerAdapter.ItemViewHolder>(mutableListOf()) {

	override fun createViewHolder(view: View) = ItemViewHolder(view)

	override fun getLayout(viewType: Int) = R.layout.item_blogpost

	inner class ItemViewHolder(private val view: View) : RecyclerViewHolder<Any>(view) {
		override fun bind(item: Any) {
			val post = item as BlogPost

			view.setOnClickListener { onBlogPostClick(post) }
			view.title.text = post.title.rendered
			view.content.text = post.content.rendered
		}
	}
}