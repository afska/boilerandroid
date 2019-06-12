package io.j3solutions.boilerandroid.utils.extensions

import android.content.res.Resources

fun Int.random() = (Math.random() * this).toInt()

fun Float.toDp(): Float = this * Resources.getSystem().displayMetrics.density
fun Int.toDp(): Float = this * Resources.getSystem().displayMetrics.density
