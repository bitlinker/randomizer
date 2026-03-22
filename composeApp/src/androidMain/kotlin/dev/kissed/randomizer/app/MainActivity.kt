package dev.kissed.randomizer.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.Modifier
import dev.kissed.randomizer.app.di.AppComponentImpl
import dev.kissed.randomizer.app.di.create

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val appComponent = AppComponentImpl::class.create()

        setContent {
            Box(modifier = Modifier.windowInsetsPadding(WindowInsets.safeContent)) {
                App(appComponent)
            }
        }
    }
}