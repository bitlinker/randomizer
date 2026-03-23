import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import dev.kissed.randomizer.app.App
import dev.kissed.randomizer.app.di.AppComponent
import dev.kissed.randomizer.app.di.WasmDatabaseFactory
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val appComponent = AppComponent.create(
        sqlDelightDriverFactory = WasmDatabaseFactory()
    )

    ComposeViewport(document.body!!) {
        App(appComponent)
    }
}