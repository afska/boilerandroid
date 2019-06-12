package io.j3solutions.boilerandroid.events

import android.os.Handler
import android.os.Looper
import com.squareup.otto.Bus

class MainThreadBus : Bus() {
    override fun post(event: Any) {
        runOnMainThread { super.post(event) }
    }
}

private val handler = Handler(Looper.getMainLooper())

private fun runOnMainThread(action: () -> (Unit)) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        action()
    } else {
        handler.post({ action() })
    }
}
