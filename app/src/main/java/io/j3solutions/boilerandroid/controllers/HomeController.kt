package io.j3solutions.boilerandroid.controllers

import android.view.View
import io.j3solutions.boilerandroid.R
import io.j3solutions.boilerandroid.views.changehandlers.VerticalChangeHandler
import kotlinx.android.synthetic.main.controller_home.view.*

class HomeController : BaseController(R.layout.controller_home) {
	override fun onViewBound(view: View) {
		view.goToPostsButton.setOnClickListener {
			goTo(PostsController(), false) {
				it
					.popChangeHandler(VerticalChangeHandler())
					.pushChangeHandler(VerticalChangeHandler())
			}
		}
	}
}