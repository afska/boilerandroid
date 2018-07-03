package io.j3solutions.boilerandroid.utils.api

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.*
import io.j3solutions.boilerandroid.BuildConfig
import io.j3solutions.boilerandroid.preferences.DelegatedPreference
import retrofit2.converter.gson.GsonConverterFactory

inline fun <reified T> newApi(
	baseUrl: String,
	progressListener: ProgressListener? = null,
	withAuthorizationHeader: Boolean = true,
	noinline makeAuthorizationHeader: ((String) -> (Pair<String, String>)) = { Pair("Authorization", it) },
	noinline makeGson: ((GsonBuilder) -> (Gson)) = { it.create() }
): T {
	val clientBuilder = okhttp3.OkHttpClient.Builder()

	if (withAuthorizationHeader) {
		clientBuilder.addNetworkInterceptor {
			val authToken: String by DelegatedPreference("")
			val (name, value) = makeAuthorizationHeader(authToken)
			val newRequest = it.request().newBuilder().header(name, value)
			it.proceed(newRequest.build())
		}
	}

	// progress listener interceptor
	if (progressListener != null) {
		clientBuilder.addNetworkInterceptor {
			val originalResponse = it.proceed(it.request())
			originalResponse.newBuilder()
				.body(ProgressResponseBody(originalResponse.body()!!, progressListener))
				.build()
		}
	}

	// logging interceptor
	val loggingInterceptor = okhttp3.logging.HttpLoggingInterceptor()
	loggingInterceptor.level = okhttp3.logging.HttpLoggingInterceptor.Level.BODY
	if (BuildConfig.DEBUG) {
		clientBuilder.addInterceptor(loggingInterceptor)
		clientBuilder.addNetworkInterceptor(StethoInterceptor())
	}

	val gsonConverter = GsonConverterFactory.create(
		makeGson(GsonBuilder())
	)

	val retrofit = retrofit2.Retrofit.Builder()
		.baseUrl(baseUrl)
		.client(clientBuilder.build())
		.addConverterFactory(gsonConverter)
		.addCallAdapterFactory(retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory.create())
		.build()

	return retrofit.create(T::class.java)
}
