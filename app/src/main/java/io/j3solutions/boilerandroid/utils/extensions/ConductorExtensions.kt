package io.j3solutions.boilerandroid.utils.extensions

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.Router

fun Router.addChangeListener(action: () -> Unit) {
    this.addChangeListener(object : ControllerChangeHandler.ControllerChangeListener {
        override fun onChangeStarted(to: Controller?, from: Controller?, isPush: Boolean, container: ViewGroup, handler: ControllerChangeHandler) {
            action()
        }

        override fun onChangeCompleted(to: Controller?, from: Controller?, isPush: Boolean, container: ViewGroup, handler: ControllerChangeHandler) {}
    })
}

fun Activity.checkPermissions(vararg permissions: String): Boolean {
    val hasPermissions = hasPermissions(*permissions)

    if (!hasPermissions)
        ActivityCompat.requestPermissions(this, permissions, 1)

    return hasPermissions
}

fun Activity.hasPermissions(vararg permissions: String): Boolean {
    return Build.VERSION.SDK_INT < 23 || permissions.all {
        checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
    }
}

fun Controller.checkPermissions(permission: String) =
    activity!!.checkPermissions(permission)
