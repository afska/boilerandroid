package io.j3solutions.boilerandroid.utils

import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener

open class OnImmediateSwipeListener(private val threshold: Float) : OnTouchListener {
    private var initialY = 0f
    private var isDragging = false

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                initialY = event.y
                isDragging = false
            }

            MotionEvent.ACTION_MOVE -> {
                val deltaY = event.y - initialY
                if (Math.abs(deltaY) > threshold) {
                    isDragging = true

                    if (deltaY < 0)
                        onImmediateSwipeUp()
                    else
                        onImmediateSwipeDown()

                    initialY = event.y
                }
            }

            MotionEvent.ACTION_UP -> {
                val wasDragging = isDragging
                isDragging = false

                return wasDragging
            }
        }

        return false
    }

    open fun onImmediateSwipeUp() {}

    open fun onImmediateSwipeDown() {}
}
