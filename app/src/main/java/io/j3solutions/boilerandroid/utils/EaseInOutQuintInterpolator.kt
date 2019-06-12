package io.j3solutions.boilerandroid.utils

import android.view.animation.Interpolator

class EaseInOutQuintInterpolator : Interpolator {
    override fun getInterpolation(t: Float): Float {
        val x: Float
        if (t < 0.5f) {
            x = t * 2.0f
            return 0.5f * x * x * x * x * x
        }
        x = (t - 0.5f) * 2 - 1
        return 0.5f * x * x * x * x * x + 1
    }
}
