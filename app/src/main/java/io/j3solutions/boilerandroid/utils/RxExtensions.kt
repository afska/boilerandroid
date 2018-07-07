package io.j3solutions.boilerandroid.utils

import com.bluelinelabs.conductor.rxlifecycle2.RxController
import io.j3solutions.boilerandroid.RootApplication
import io.j3solutions.boilerandroid.utils.api.RequestFailedException
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import okio.Okio
import retrofit2.Response
import timber.log.Timber
import java.io.File

fun <T> Single<Response<T>>.asApiCall(controller: RxController? = null, cache: Boolean = true): Single<T> {
	var result = this
		.setUpThreads(controller)
		.flatMap {
			if (it.isSuccessful) Single.just(it.body()!!)
			else {
				val errorBody = try { it.errorBody()!!.string().fromJson() } catch(e: Exception) { null }
				Single.error(RequestFailedException(it.code(), errorBody))
			}
		}
	if (cache) result = result.cache()

	return result.doOnError { Timber.e(it, "API error encountered") }
}

fun Single<ResponseBody>.saveToFile(controller: RxController, path: String): Single<File> =
	this
		.setUpThreads(controller)
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
			}.setUpThreads(controller)
		}
		.cache()

fun <T> newSingle(action: () -> (T)): Single<T> {
	return Single.create<T> {
		try {
			val result = action()
			it.onSuccess(result)
		} catch(e: RuntimeException) {
			it.onError(e)
		}
	}.setUpThreads()
}

fun <T> Single<T>.setUpThreads(controller: RxController? = null): Single<T> {
	val result = if (controller == null) this else this.compose(controller.bindToLifecycle())
	return result
		.subscribeOn(Schedulers.io())
		.unsubscribeOn(Schedulers.io())
		.observeOn(AndroidSchedulers.mainThread())
}

fun <T> Flowable<T>.setUpThreads(controller: RxController? = null): Flowable<T> {
	val result = if (controller == null) this else this.compose(controller.bindToLifecycle())
	return result
		.subscribeOn(Schedulers.io())
		.unsubscribeOn(Schedulers.io())
		.observeOn(AndroidSchedulers.mainThread())
}

fun <T> Maybe<T>.setUpThreads(controller: RxController? = null): Maybe<T> {
	val result = if (controller == null) this else this.compose(controller.bindToLifecycle())
	return result
		.subscribeOn(Schedulers.io())
		.unsubscribeOn(Schedulers.io())
		.observeOn(AndroidSchedulers.mainThread())
}

fun <T> Flowable<T>.subscribeOnce(controller: RxController? = null, action: (T) -> Unit): Disposable =
	this.firstElement().subscribe(controller, action)

fun <T> Flowable<T>.subscribe(controller: RxController? = null, action: (T) -> Unit): Disposable =
	this.setUpThreads(controller).subscribe(fun(it) {
		if (controller != null && controller.activity == null)
			return
		action(it)
	}, { Timber.e(it) })

fun <T> Single<T>.subscribe(controller: RxController? = null, action: (T) -> Unit): Disposable =
	this.setUpThreads(controller).subscribe(fun(it) {
		if (controller != null && controller.activity == null)
			return
		action(it)
	}, { Timber.e(it) })

fun <T> Maybe<T>.subscribe(controller: RxController? = null, action: (T) -> Unit): Disposable =
	this.setUpThreads(controller).subscribe(fun(it) {
		if (controller != null && controller.activity == null)
			return
		action(it)
	}, { Timber.e(it) })
