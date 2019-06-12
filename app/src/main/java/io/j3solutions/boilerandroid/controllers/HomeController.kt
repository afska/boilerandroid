package io.j3solutions.boilerandroid.controllers

import android.view.View
import io.j3solutions.boilerandroid.R
import io.j3solutions.boilerandroid.services.ApiUpdateService
import kotlinx.android.synthetic.main.controller_home.view.*

class HomeController : BaseController(R.layout.controller_home) {
    override fun onViewBound(view: View) {
        ApiUpdateService.start()

        view.goToPostsButton.setOnClickListener {
            transitionTo(BlogPostsController(), false)
        }
    }
}