package io.j3solutions.boilerandroid.utils.api

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.j3solutions.boilerandroid.BuildConfig
import retrofit2.converter.gson.GsonConverterFactory

inline fun <reified T> newApi(
    baseUrl: String,
    progressListener: ProgressListener? = null,
    noinline withAccessToken: (() -> (String))? = null,
    noinline makeGson: ((GsonBuilder) -> (Gson)) = { it.create() }
): T {
    val clientBuilder = okhttp3.OkHttpClient.Builder()

    if (withAccessToken != null) {
        clientBuilder.addNetworkInterceptor {
            val accessToken = withAccessToken()
            val (name, value) = Pair("Authorization", "Bearer $accessToken")
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
        makeGson(
            GsonBuilder()
                .serializeNulls()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        )
    )

    val retrofit = retrofit2.Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(clientBuilder.build())
        .addConverterFactory(gsonConverter)
        .addCallAdapterFactory(retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory.create())
        .build()

    return retrofit.create(T::class.java)
}
