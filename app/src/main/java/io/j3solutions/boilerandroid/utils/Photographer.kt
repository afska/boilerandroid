package io.j3solutions.boilerandroid.utils

import android.app.Activity
import android.content.Intent
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import com.bluelinelabs.conductor.Controller
import io.reactivex.Single
import io.reactivex.SingleEmitter
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class Photographer {
	companion object {
		const val REQUEST_CAMERA_CAPTURE = 2912
	}

	private var currentPromiseEmitter: SingleEmitter<File>? = null
	private var currentPhotoPath = ""

	fun takePhoto(controller: Controller, fileProvider: String): Single<File> {
		val activity = controller.activity!!

		val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
		if (takePictureIntent.resolveActivity(activity.packageManager) != null) {
			val photoFile = createImageFile(activity)

			val photoUri = FileProvider.getUriForFile(activity, fileProvider, photoFile)
			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
			controller.startActivityForResult(takePictureIntent, REQUEST_CAMERA_CAPTURE)
		}

		return Single.create<File> {
			currentPromiseEmitter = it
		}
	}

	fun handleOnActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
		if (requestCode == REQUEST_CAMERA_CAPTURE) {
			val currentPromiseEmitter = currentPromiseEmitter ?: return

			if (resultCode == Activity.RESULT_OK) {
				val file = File(currentPhotoPath)
				currentPromiseEmitter.onSuccess(file)
			} else {
				currentPromiseEmitter.onError(RuntimeException("Photo request failed. Result code: ${resultCode}"))
			}

			clearState()
		}
	}

	private fun createImageFile(activity: Activity): File {
		val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
		val imageFileName = "JPEG_" + timeStamp + "_"
		val storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
		val image = File.createTempFile(imageFileName, ".jpg", storageDir)

		currentPhotoPath = image.absolutePath

		return image
	}

	private fun clearState() {
		currentPromiseEmitter = null
		currentPhotoPath = ""
	}
}
