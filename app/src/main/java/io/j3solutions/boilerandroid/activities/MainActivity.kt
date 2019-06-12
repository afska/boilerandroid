package io.j3solutions.boilerandroid.activities

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import io.j3solutions.boilerandroid.R
import io.j3solutions.boilerandroid.controllers.HomeController
import io.j3solutions.boilerandroid.events.Events
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onStart() {
        super.onStart()
        Events.bus.register(this)
    }

    override fun onStop() {
        super.onStop()
        Events.bus.unregister(this)
    }

    private lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
            .cancelAll()

        setContentView(R.layout.activity_main)
        setUpRouter(savedInstanceState)
    }

    override fun onBackPressed() {
        if (!router.handleBack())
            super.onBackPressed()
    }

    private fun setUpRouter(savedInstanceState: Bundle?) {
        router = Conductor.attachRouter(this, container, savedInstanceState)
        setUpInitialController(HomeController())
    }

    private fun setUpInitialController(controller: Controller, force: Boolean = false) {
        if (!router.hasRootController() || force) {
            router.setRoot(
                RouterTransaction.with(controller)
            )
        }
    }
}
