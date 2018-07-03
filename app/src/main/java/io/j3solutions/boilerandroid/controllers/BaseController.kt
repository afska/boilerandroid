package io.j3solutions.boilerandroid.controllers

import android.arch.lifecycle.MutableLiveData
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.rxlifecycle2.RxController
import com.evernote.android.state.StateSaver
import io.j3solutions.boilerandroid.RootApplication
import io.j3solutions.boilerandroid.utils.asApiCall
import io.reactivex.Single
import retrofit2.Response
import timber.log.Timber

open class BaseController(private val layout: Int) : RxController() {
	var isLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
		Timber.v("Creating controller: ${this::class.java.simpleName}")
		val view = inflater.inflate(layout, container, false)
		onViewBound(view)
		return view
	}

	open fun onViewBound(view: View) {}

	fun goTo(controller: Controller, setRoot: Boolean = false, transactionAnimation: (RouterTransaction) -> RouterTransaction = { it }) {
		val transaction = transactionAnimation(RouterTransaction.with(controller))

		if (!setRoot) router.pushController(transaction)
		else router.setRoot(transaction)
	}

	fun <T> http(single: Single<Response<T>>, cache: Boolean = true): Single<T> {
		return single.asApiCall(this, cache)
	}

	fun <T> withLoading(single: Single<T>): Single<T> {
		isLoading.value = true

		val stopLoading: () -> (Unit) = { isLoading.value = false }
		single.subscribe({ stopLoading() }, { stopLoading() })

		return single
	}

	fun showToast(text: String) {
		Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show()
	}

	fun translate(id: Int): String = RootApplication.appContext.resources.getString(id)

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		StateSaver.saveInstanceState(this, outState)
	}

	override fun onRestoreInstanceState(savedInstanceState: Bundle) {
		super.onRestoreInstanceState(savedInstanceState)
		StateSaver.restoreInstanceState(this, savedInstanceState)
	}
}
