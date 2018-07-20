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

	private var currentBitmapEmitter: SingleEmitter<Bitmap>? = null
	private var currentFileEmitter: SingleEmitter<File>? = null
	private var currentPhotoPath = ""

	/**
	 * Takes a photo and get the thumbnail as a *Bitmap*.
	 */
	fun takePhoto(controller: Controller): Single<Bitmap> {
		val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
		controller.startActivityForResult(takePictureIntent, REQUEST_CAMERA_CAPTURE)

		return Single.create<Bitmap> {
			currentBitmapEmitter = it
		}
	}

	/**
	 * Takes a photo and retrieves the full-size image as a *File*.
	 */
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
			currentFileEmitter = it
		}
	}

	fun handleOnActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
		if (requestCode == REQUEST_CAMERA_CAPTURE) {
			when {
				currentBitmapEmitter != null -> {
					val currentPromiseEmitter = currentBitmapEmitter ?: return

					if (resultCode == Activity.RESULT_OK) {
						val bitmap = intent!!.extras.get("data") as Bitmap
						currentPromiseEmitter.onSuccess(bitmap)
					} else fail(currentPromiseEmitter, resultCode)
				}

				currentFileEmitter != null -> {
					val currentPromiseEmitter = currentFileEmitter ?: return

					if (resultCode == Activity.RESULT_OK) {
						val file = File(currentPhotoPath)
						currentPromiseEmitter.onSuccess(file)
					} else fail(currentPromiseEmitter, resultCode)
				}
			}

			clearState()
		}
	}

	private fun <T> fail(emitter: SingleEmitter<T>, resultCode: Int) {
		emitter.onError(RuntimeException("Photo request failed. Result code: $resultCode"))
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
		currentBitmapEmitter = null
		currentFileEmitter = null
		currentPhotoPath = ""
	}
}
