package io.j3solutions.boilerandroid.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

fun EditText.setTextWithCursor(text: String) {
	setText(text)
	try { setSelection(text.length) } catch(e: Exception) { }
}

fun EditText.addOnTextChangedListener(action: () -> (Unit)) {
	addTextChangedListener(object : TextWatcher {
		override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
			action()
		}

		override fun afterTextChanged(p0: Editable?) {
		}

		override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
		}
	})
}
