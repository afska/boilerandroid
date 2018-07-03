package io.j3solutions.boilerandroid.utils

import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation

fun View.fadeIn(time: Long = 500) {
	if (alpha == 1f) return
	clearAnimation()
	animate().setDuration(time).alpha(1f)
}

fun View.fadeOut(time: Long = 500) {
	if (alpha == 0f) return
	clearAnimation()
	animate().setDuration(time).alpha(0f)
}

fun View.expand() {
	this.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
	val target = this.measuredHeight

	this.layoutParams.height = 0
	this.visibility = View.VISIBLE
	val a = object : Animation() {
		override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
			this@expand.layoutParams.height = if (interpolatedTime == 1f)
				ViewGroup.LayoutParams.WRAP_CONTENT
			else
				(target * interpolatedTime).toInt()
			this@expand.requestLayout()
		}

		override fun willChangeBounds(): Boolean {
			return true
		}
	}

	a.duration = (target / this.context.resources.displayMetrics.density).toLong()
	this.startAnimation(a)
}

fun View.collapse() {
	val initialHeight = this.measuredHeight

	val a = object : Animation() {
		override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
			if (interpolatedTime == 1f) {
				this@collapse.visibility = View.GONE
			} else {
				this@collapse.layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
				this@collapse.requestLayout()
			}
		}

		override fun willChangeBounds(): Boolean {
			return true
		}
	}

	a.duration = (initialHeight / this.context.resources.displayMetrics.density).toLong()
	this.startAnimation(a)
}
