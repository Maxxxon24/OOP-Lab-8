package component

import hoc.withDisplayName
import kotlinx.html.*
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*

interface EditPageProps<Z> : RProps {
    var path:String
}

fun <Z> editPage(AnyListRedactProps: AnyListRedactProps<Z>) =
    functionalComponent<EditPageProps <Z> > { props ->
        val current = props.path.substringAfter('/')
        div{
            h3 { +"Страница редактирования" }

            span { +"Изменить: " }
            input(InputType.number) {
                attrs.id = "Edit${current}"
                attrs.placeholder = "edit index"
            }
            input(InputType.text) {
                attrs.id = "${current}Full"
                attrs.placeholder = "enter $current"
            }
            button{
                +"Edit $current"
                attrs.onClickFunction = AnyListRedactProps.editFunction
            }

            br {  }
            span { +"Удалить  : " }
            input(InputType.number) {
                attrs.id = "${current}Delete"
                attrs.placeholder = "delete by index"
            }
            button{
                +"delete $current"
                attrs.onClickFunction = AnyListRedactProps.removeFunction
            }

            br {  }
            span { +"Добавить: " }
            input(InputType.text) {
                attrs.id = "${current}Add"
                attrs.placeholder = "enter $current"
            }
            button{
                +"Add $current"
                attrs.onClickFunction = AnyListRedactProps.newFunction
            }

        }
    }

fun <Z> RBuilder.editPage(
    input:AnyListRedactProps<Z>
) = child(withDisplayName("editPage", component.editPage(input)))
{ attrs.path = input.path }