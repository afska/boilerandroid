package io.j3solutions.boilerandroid.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import com.evernote.android.state.StateSaver
import io.j3solutions.boilerandroid.RootApplication
import io.j3solutions.boilerandroid.utils.extensions.asApiCall
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Response
import timber.log.Timber

open class BaseController(private val layout: Int) : Controller() {
    var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val disposables = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        Timber.v("Creating controller: ${this::class.java.simpleName}")
        val view = inflater.inflate(layout, container, false)
        onViewBound(view)
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    open fun onViewBound(view: View) {}

    private fun goTo(
        controller: Controller,
        setRoot: Boolean = false,
        transactionAnimation: (RouterTransaction) -> RouterTransaction = { it }
    ) {
        val transaction = transactionAnimation(RouterTransaction.with(controller))

        if (!setRoot) router.pushController(transaction)
        else router.setRoot(transaction)
    }

    fun transitionTo(controller: Controller, setRoot: Boolean = false) {
        goTo(controller, setRoot) {
            it
                .popChangeHandler(HorizontalChangeHandler())
                .pushChangeHandler(HorizontalChangeHandler())
        }
    }

    fun <T> http(single: Single<Response<T>>, cache: Boolean = true): Single<T> {
        return single.asApiCall(cache)
    }

    fun <T> withLoading(single: Single<T>): Single<T> {
        isLoading.value = true
        return single.doFinally { isLoading.postValue(false) }
    }

    fun showToast(text: String) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show()
    }

    fun translate(id: Int, vararg args: Any): String =
        RootApplication.appContext.resources.getString(id, *args)

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        StateSaver.saveInstanceState(this, outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        StateSaver.restoreInstanceState(this, savedInstanceState)
    }
}
