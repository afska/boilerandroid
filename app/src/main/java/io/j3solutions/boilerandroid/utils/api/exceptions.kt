package io.j3solutions.boilerandroid.utils.api

import java.lang.RuntimeException

class RequestFailedException(val statusCode: Int, body: Map<String, String>?) : RuntimeException()
