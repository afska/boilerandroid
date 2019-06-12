// ktlint-disable filename
package io.j3solutions.boilerandroid.utils.api

import com.google.gson.JsonObject

class RequestFailedException(val statusCode: Int, val body: JsonObject?) : RuntimeException()
