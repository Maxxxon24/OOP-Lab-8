package component

import hoc.withDisplayName
import org.w3c.dom.events.Event
import react.*
import react.dom.*

interface AnyListRedactProps<Z> : RProps {
    var list: Array<Z>
    var name: String
    var path: String
    var newFunction: EFT
    var editFunction:  EFT
    var del:(Int) -> (Event) -> Unit
}

fun <Z> anyListRedactFC(
        redactPage: RBuilder.(AnyListRedactProps<Z>) -> ReactElement,
        currentList: RBuilder.(Array<Z>, String, String,(Int) -> (Event) -> Unit,Boolean) -> ReactElement
) = functionalComponent<AnyListRedactProps<Z>> {
    val temp = it
    div {
        redactPage( temp )
        currentList( temp.list, temp.name, temp.path, {a -> temp.del(a) },true )
    }
}

fun <Z> RBuilder.anyListRedact(
        name: String,
        path: String,
        redactComponent: RBuilder.(AnyListRedactProps<Z>) -> ReactElement,
        listComponent: RBuilder.(Array<Z>, String, String,(Int) -> (Event) -> Unit,Boolean) -> ReactElement,
        list: Array<Z>,
        removeFunction: EFT,
        editFunction: EFT,
        newFunction: EFT,
        del:(Int) -> (Event) -> Unit
) = child(withDisplayName(name, anyListRedactFC(redactComponent, listComponent))){
    attrs.list = list
    attrs.name = name
    attrs.path = path
    attrs.editFunction = editFunction
    attrs.newFunction = newFunction
    attrs.del = del
}
