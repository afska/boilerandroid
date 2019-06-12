package io.j3solutions.boilerandroid.utils.extensions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun View.show() {
    visibility = VISIBLE
}

fun View.hide() {
    visibility = GONE
}

fun View.setVisible(makeVisible: Boolean) {
    if (makeVisible) show() else hide()
}

fun View.setTransparency(makeVisible: Boolean) {
    if (makeVisible) show() else makeInvisible()
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

fun View.setVisibilityWithFade(makeVisible: Boolean) {
    if (makeVisible) fadeIn() else fadeOut(true)
}

fun View.isVisible(): Boolean {
    return visibility == VISIBLE
}

fun View.changeBackgroundColor(color: String) {
    val background = this.background as StateListDrawable
    val gradient = background.current as GradientDrawable
    gradient.setColor(color.toColor())
}

fun View.getActivity(): Activity? {
    var context = context
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }
    return null
}

fun View.getScreenWidth(): Float {
    val displayMetrics = resources.displayMetrics
    return displayMetrics.widthPixels / displayMetrics.density
}

fun EditText.showKeyboard(context: Context) {
    requestFocus()
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
}

fun EditText.hideKeyboard(context: Context) {
    clearFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}
