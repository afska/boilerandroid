package io.j3solutions.boilerandroid.utils.extensions

import android.app.Activity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.subscribeNotNull(owner: Activity?, action: (T) -> (Unit)) {
    observe(owner as LifecycleOwner, Observer<T> { action(it!!) })
}
