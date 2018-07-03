package io.j3solutions.boilerandroid.utils

import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import io.j3solutions.boilerandroid.R

fun View.show() {
	visibility = VISIBLE
}

fun View.hide() {
	visibility = GONE
}

fun View.setVisible(isVisible: Boolean) {
	if (isVisible) show() else hide()
}

fun View.setTransparency(isVisible: Boolean) {
	if (isVisible) show() else makeInvisible()
}

fun View.makeInvisible() {
	visibility = View.INVISIBLE
}

fun View.toggleVisibility() {
	if (isVisible()) hide() else show()
}

fun View.toggleVisibilityWithExpansion() {
	if (isVisible()) collapse() else expand()
}

fun View.isVisible(): Boolean {
	return visibility == VISIBLE
}

fun View.changeBackgroundColor(color: Int) {
	((background as LayerDrawable)
		.getDrawable(0) as GradientDrawable)
		.setColor(ContextCompat.getColor(context, color))
}
