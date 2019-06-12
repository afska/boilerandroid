package io.j3solutions.boilerandroid.utils.extensions

import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import androidx.core.view.updateLayoutParams

fun View.fadeIn(time: Long = 150) {
    clearAnimation()
    show()
    animate().setDuration(time).alpha(1f)
}

fun View.fadeOut(hideAtEnd: Boolean = false, time: Long = 150) {
    clearAnimation()
    if (hideAtEnd)
        animate().setDuration(time).alpha(0f).withEndAction(this::hide)
    else
        animate().setDuration(time).alpha(0f)
}

fun View.growHorizontally(endWidth: Int) {
    val originalWidth = this.width
    val growth = endWidth - originalWidth

    val animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            this@growHorizontally.updateLayoutParams {
                width = (growth * interpolatedTime).toInt() + originalWidth
            }

            this@growHorizontally.requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    animation.duration = 100
    this.startAnimation(animation)
    this.requestLayout()
}

fun View.expand() {
    this.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    val target = this.measuredHeight

    this.layoutParams.height = 0
    this.visibility = View.VISIBLE
    val animation = object : Animation() {
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

    animation.duration = (target / this.context.resources.displayMetrics.density).toLong()
    this.startAnimation(animation)
}

fun View.collapse() {
    val initialHeight = this.measuredHeight

    val animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            if (interpolatedTime == 1f) {
                this@collapse.visibility = View.GONE
            } else {
                this@collapse.layoutParams.height =
                    initialHeight - (initialHeight * interpolatedTime).toInt()
                this@collapse.requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    animation.duration = (initialHeight / this.context.resources.displayMetrics.density).toLong()
    this.startAnimation(animation)
}
