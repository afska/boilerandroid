package io.j3solutions.boilerandroid.utils

import android.content.Context
import android.content.ContextWrapper

/**
 * Fixes a leak caused by AudioManager using an Activity context.
 * Tracked at https://android-review.googlesource.com/#/c/140481/1 and
 * https://github.com/square/leakcanary/issues/205
 */
class AudioServiceActivityLeak internal constructor(base: Context) : ContextWrapper(base) {

    override fun getSystemService(name: String): Any {
        return if (Context.AUDIO_SERVICE == name) {
            applicationContext.getSystemService(name)
        } else super.getSystemService(name)
    }

    companion object {

        fun preventLeakOf(base: Context): ContextWrapper {
            return AudioServiceActivityLeak(base)
        }
    }
}
