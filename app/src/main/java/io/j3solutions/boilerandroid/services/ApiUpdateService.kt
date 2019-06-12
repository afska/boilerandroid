package io.j3solutions.boilerandroid.services

import android.app.IntentService
import android.content.Intent
import io.j3solutions.boilerandroid.RootApplication
import io.j3solutions.boilerandroid.api.BlogPostsApi
import io.j3solutions.boilerandroid.persistence.Database
import io.j3solutions.boilerandroid.utils.extensions.asApiCall
import io.j3solutions.boilerandroid.utils.extensions.then
import io.reactivex.rxkotlin.plusAssign
import timber.log.Timber

class ApiUpdateService : IntentService("ApiUpdateService") {
    companion object {
        fun start() {
            val apiUpdate = Intent(RootApplication.appContext, ApiUpdateService::class.java)
            RootApplication.appContext.startService(apiUpdate)
        }

        fun updateFromApi() {
            Timber.d("Api Update Service Started!")

            BlogPostsApi.api.getAll().asApiCall().then {
                Database.instance { db ->
                    Timber.d("BEGINNING TRANSACTION")
                    db.runInTransaction {
                        db.blogPostDao().insertAll(it)
                    }
                    Timber.d("ENDING TRANSACTION")
                }
            }
        }
    }

    override fun onHandleIntent(workIntent: Intent?) {
        updateFromApi()
    }
}
