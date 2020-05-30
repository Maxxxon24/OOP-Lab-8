package component

import hoc.withDisplayName
import react.*
import react.dom.*

interface AnyListRedactProps<Z> : RProps {
    var list: Array<Z>
    var name: String
    var path: String
    var newFunction: EFT
    var removeFunction: EFT
    var editFunction:  EFT
}

fun <Z> anyListRedactFC(
    redactPage: RBuilder.(AnyListRedactProps<Z>) -> ReactElement,
    currentList: RBuilder.(Array<Z>, String, String) -> ReactElement
) = functionalComponent<AnyListRedactProps<Z>> {
    val temp = it
        div {
            redactPage( temp )
            currentList( temp.list, temp.name, temp.path )
        }
    }

fun <Z> RBuilder.anyListRedact(
    name: String,
    path: String,
    redactComponent: RBuilder.(AnyListRedactProps<Z>) -> ReactElement,
    listComponent: RBuilder.(Array<Z>, String, String) -> ReactElement,
    list: Array<Z>,
    removeFunction: EFT,
    editFunction: EFT,
    newFunction: EFT
) = child(withDisplayName(name, anyListRedactFC(redactComponent, listComponent))){
        attrs.list = list
        attrs.name = name
        attrs.path = path
        attrs.removeFunction = removeFunction
        attrs.editFunction = editFunction
        attrs.newFunction = newFunction
}
