package io.j3solutions.boilerandroid.activities

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import io.j3solutions.boilerandroid.R
import io.j3solutions.boilerandroid.controllers.BaseController
import io.j3solutions.boilerandroid.controllers.HomeController
import io.j3solutions.boilerandroid.events.Events
import io.j3solutions.boilerandroid.utils.addChangeListener
import io.reactivex.Single
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
	private lateinit var router: Router

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		(getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
			.cancelAll()

		window.setFlags(
			WindowManager.LayoutParams.FLAG_FULLSCREEN,
			WindowManager.LayoutParams.FLAG_FULLSCREEN
		)

		setContentView(R.layout.activity_main)
		setUpRouter(savedInstanceState)
	}

	override fun onStart() {
		super.onStart()

		Events.bus.register(this)
	}

	override fun onStop() {
		Events.bus.unregister(this)
		super.onStop()
	}

	override fun onBackPressed() {
		if (!router.handleBack())
			super.onBackPressed()
	}

	private fun setUpInitialController(controller: Controller, force: Boolean = false) {
		if (!router.hasRootController() || force) {
			router.setRoot(
				RouterTransaction.with(controller)
			)
		}
	}

	private fun getInitialController(): Single<BaseController> {
		return Single.just(HomeController())
	}

	private fun setUpRouter(savedInstanceState: Bundle?) {
		router = Conductor.attachRouter(this, container, savedInstanceState)
		router.addChangeListener {
			// ApiUpdateService.start()
		}

		getInitialController().subscribe({
			setUpInitialController(it)
		}, {})
	}
}
