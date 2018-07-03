package io.j3solutions.boilerandroid

import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho
import timber.log.Timber

class RootApplication : Application() {
	companion object {
		lateinit var appContext: Context
	}

	override fun onCreate() {
		super.onCreate()

		if (BuildConfig.DEBUG) {
			Stetho.initializeWithDefaults(this)
			Timber.plant(Timber.DebugTree())
		}

		appContext = applicationContext
	}
}
