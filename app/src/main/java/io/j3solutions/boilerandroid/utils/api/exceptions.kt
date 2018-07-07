package io.j3solutions.suterhandroid.utils.api

import com.google.gson.JsonObject
import java.lang.RuntimeException

class RequestFailedException(val statusCode: Int, val body: JsonObject?) : RuntimeException()
