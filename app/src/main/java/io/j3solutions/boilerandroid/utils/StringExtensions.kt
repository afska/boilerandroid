package io.j3solutions.boilerandroid.utils

import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import com.bluelinelabs.conductor.Controller
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.j3solutions.boilerandroid.RootApplication

private val HEX_CHARS = "0123456789ABCDEF"

fun String.hexStringToByteArray(): ByteArray {
	val uppercase = this.toUpperCase()
	val result = ByteArray(length / 2)

	for (i in 0 until length step 2) {
		val firstIndex = HEX_CHARS.indexOf(uppercase[i]);
		val secondIndex = HEX_CHARS.indexOf(uppercase[i + 1]);

		val octet = firstIndex.shl(4).or(secondIndex)
		result.set(i.shr(1), octet.toByte())
	}

	return result
}

fun String.getFilenameFromURI(): String {
	return this.substringAfterLast("/")
}

fun String.onlyDigits() = this.replace(Regex("\\D+"), "")

fun String.fromJson(): Map<String, String> {
	val type = object : TypeToken<Map<String, String>>() {}.getType()
	return Gson().fromJson<Map<String, String>>(this, type)
}

fun String.getContactNameOrNumber(controller: Controller): String {
	var contactName: String = this

	if (!controller.checkPermissions(android.Manifest.permission.READ_CONTACTS))
		return contactName

	var cursor: Cursor? = null
	try {
		val uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(this.onlyDigits()))
		val projection = arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME)
		cursor = RootApplication.appContext.contentResolver.query(uri, projection, null, null, null)
		if (cursor!!.moveToFirst()) contactName = cursor.getString(0)
	} catch(e: Exception) {
	} finally {
		cursor?.close()
	}

	return contactName
}

fun String.interlaceWithSpaces(): String {
	return split("").joinToString(" ")
}
