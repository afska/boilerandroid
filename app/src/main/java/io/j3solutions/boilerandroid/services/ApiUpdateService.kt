package io.j3solutions.boilerandroid.services

import android.app.IntentService
import android.content.Intent
import io.j3solutions.boilerandroid.RootApplication
import io.j3solutions.boilerandroid.api.BlogPostsApi
import io.j3solutions.boilerandroid.persistence.Db
import io.j3solutions.boilerandroid.utils.asApiCall
import timber.log.Timber

class ApiUpdateService : IntentService("ApiUpdateService") {
	companion object {
		fun start() {
			val apiUpdate = Intent(RootApplication.appContext, ApiUpdateService::class.java)
			RootApplication.appContext.startService(apiUpdate)
		}

		fun updateFromApi() {
			Timber.d("Api Update Service Started!")

			BlogPostsApi.api.getAll().asApiCall().subscribe({
				Db.instance { db ->
					Timber.d("BEGINNING TRANSACTION")
					db.beginTransaction()
					db.blogPostDao().insertAll(it)
					db.setTransactionSuccessful()
					db.endTransaction()
					Timber.d("ENDING TRANSACTION")
				}
			}, {
				Timber.e(it, "Error retrieving data")
			})
		}
	}

	override fun onHandleIntent(workIntent: Intent?) {
		updateFromApi()
	}
}
