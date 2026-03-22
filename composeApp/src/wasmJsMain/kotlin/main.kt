import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import dev.kissed.randomizer.app.App
import dev.kissed.randomizer.app.di.AppComponentImpl
import dev.kissed.randomizer.app.di.create
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val appComponent = AppComponentImpl::class.create()

    ComposeViewport(document.body!!) {
        App(appComponent)
    }
}