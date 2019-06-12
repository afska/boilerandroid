package io.j3solutions.boilerandroid.utils.extensions

import com.bluelinelabs.conductor.Controller
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.j3solutions.boilerandroid.RootApplication
import io.j3solutions.boilerandroid.utils.api.RequestFailedException
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import okio.Okio
import retrofit2.Response
import timber.log.Timber
import java.io.File

@Suppress("UNCHECKED_CAST")
fun <T> Single<Response<T>>.asApiCall(cache: Boolean = true): Single<T> {
    var result = this
        .setUpThreads()
        .flatMap {
            if (it.isSuccessful) {
                val body = it.body()
                Single.just(body ?: "" as T)
            } else {
                val errorBody = try {
                    JsonParser().parse(it.errorBody()!!.string()) as JsonObject
                } catch (e: Exception) {
                    null
                }

                Single.error(RequestFailedException(it.code(), errorBody))
            }
        }
    if (cache) result = result.cache()

    return result.doOnError { Timber.e(it, "API error encountered") }
}

fun Single<ResponseBody>.saveToFile(path: String): Single<File> =
    this
        .setUpThreads()
        .flatMap { body ->
            Single.create<File> {
                try {
                    val appDirectory = RootApplication.appContext.cacheDir

                    val file = File("$appDirectory/$path")
                    File(file.parent).mkdir()

                    val bufferedSink = Okio.buffer(Okio.sink(file))
                    bufferedSink.writeAll(body.source())
                    bufferedSink.close()

                    it.onSuccess(file)
                } catch (e: Exception) {
                    it.onError(e)
                }
            }.setUpThreads()
        }
        .cache()

fun <T> newSingle(action: () -> (T)): Single<T> {
    return Single.create<T> {
        try {
            val result = action()
            it.onSuccess(result)
        } catch (e: RuntimeException) {
            it.onError(e)
        }
    }.setUpThreads()
}
fun <T> Single<T>.setUpThreads(): Single<T> {
    return this
        .subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Flowable<T>.setUpThreads(): Flowable<T> {
    return this
        .subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Maybe<T>.setUpThreads(): Maybe<T> {
    return this
        .subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

@CheckReturnValue
fun <T> Flowable<T>.subscribeOnce(action: (T) -> Unit): Disposable =
    this.firstElement().subscribe(action)

@CheckReturnValue
fun <T> Single<T>.execute(): Disposable = setUpThreads().subscribe({}, {})

@CheckReturnValue
fun <T> Single<T>.then(
    controller: Controller? = null,
    onError: (Throwable) -> (Unit) = { },
    action: (T) -> Unit = {}
): Disposable =
    this.setUpThreads().subscribe(fun(it) {
        if (controller != null && controller.activity == null)
            return
        action(it)
    }, {
        onError(it)
        Timber.e(it)
    })

@CheckReturnValue
fun <T> Flowable<T>.then(controller: Controller? = null, action: (T) -> Unit): Disposable =
    this.setUpThreads().subscribe(fun(it) {
        if (controller != null && controller.activity == null)
            return
        action(it)
    }, { Timber.e(it) })
