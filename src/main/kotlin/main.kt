import component.app
import react.dom.render
import react.router.dom.hashRouter
import kotlin.browser.document

fun main() {
    render(document.getElementById("root")) {
        hashRouter {
            app()
        }
    }
}