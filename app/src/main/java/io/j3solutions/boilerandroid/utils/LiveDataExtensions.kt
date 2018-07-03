package io.j3solutions.boilerandroid.utils

import android.app.Activity
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer

fun <T> LiveData<T>.subscribeNotNull(owner: Activity?, action: (T) -> (Unit)) {
	observe(owner as LifecycleOwner, Observer<T>({ action(it!!) }))
}
