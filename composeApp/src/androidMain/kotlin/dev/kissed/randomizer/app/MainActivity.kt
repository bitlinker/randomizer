package dev.kissed.randomizer.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dev.kissed.randomizer.app.di.AppComponentImpl
import dev.kissed.randomizer.app.di.create

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appComponent = AppComponentImpl::class.create()

        setContent {
            App(appComponent)
        }
    }
}