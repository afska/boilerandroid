package io.j3solutions.boilerandroid.utils.extensions

import android.content.res.Resources
import android.util.TypedValue

fun Float.pixels(resources: Resources): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        resources.displayMetrics
    ).toInt()
}
