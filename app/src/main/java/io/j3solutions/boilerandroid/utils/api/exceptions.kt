package io.j3solutions.boilerandroid.utils.api

import com.google.gson.JsonObject
import java.lang.RuntimeException

class RequestFailedException(val statusCode: Int, val body: JsonObject?) : RuntimeException()
