package io.j3solutions.boilerandroid.utils

import com.bumptech.glide.DrawableTypeRequest
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.lang.Exception

fun <T> DrawableTypeRequest<T>.onFinish(action: () -> (Unit)): DrawableTypeRequest<T> {
	this.listener(object : RequestListener<T, GlideDrawable> {
		override fun onResourceReady(resource: GlideDrawable?, model: T?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
			action()
			return false
		}

		override fun onException(e: Exception?, model: T?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
			action()
			return false
		}
	})

	return this
}