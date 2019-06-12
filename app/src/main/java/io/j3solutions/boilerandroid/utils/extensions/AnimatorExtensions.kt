package io.j3solutions.boilerandroid.utils.extensions

import android.animation.Animator
import android.animation.AnimatorSet

/**
 * Fixes this bug: If you run an Animator inside an AnimatorSet, its duration is ignored.
 */
fun Animator.asSet(duration: Long): AnimatorSet {
    val animatorSet = AnimatorSet()
    animatorSet.duration = duration
    animatorSet.playTogether(this)
    return animatorSet
}

class RepeatListener(val times: Int, val callback: () -> (Unit) = {}) : Animator.AnimatorListener {
    var count = 0

    override fun onAnimationRepeat(p0: Animator?) {
    }

    override fun onAnimationEnd(animation: Animator) {
        count++
        if (count < times) animation.start()
        else callback()
    }

    override fun onAnimationCancel(p0: Animator?) {
    }

    override fun onAnimationStart(p0: Animator?) {
    }
}
