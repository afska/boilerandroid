package io.j3solutions.boilerandroid.utils

import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.PermissionChecker.checkSelfPermission
import android.view.ViewGroup
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

fun Controller.loadFont(fileName: String): Typeface =
	Typeface.createFromAsset(this.activity!!.assets, "fonts/$fileName")

fun Fragment.loadFont(fileName: String): Typeface =
	Typeface.createFromAsset(this.activity!!.assets, "fonts/$fileName")

fun Activity.checkPermissions(vararg permissions: String): Boolean {
	val hasPermissions = hasPermissions(*permissions)

	if (!hasPermissions)
		ActivityCompat.requestPermissions(this, permissions, 1)

	return hasPermissions
}

fun Activity.hasPermissions(vararg permissions: String): Boolean {
	if (Build.VERSION.SDK_INT >= 23) {
		val hasAllPermissions = permissions.all {
			checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
		}

		return hasAllPermissions
	} else {
		return true
	}
}

fun Controller.checkPermissions(permission: String) =
	activity!!.checkPermissions(permission)
