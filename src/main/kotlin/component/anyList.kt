package component

import data.Student
import hoc.withDisplayName
import kotlinx.html.id
import kotlinx.html.js.onClickFunction
import org.w3c.dom.events.Event
import react.*
import react.dom.*
import react.functionalComponent
import react.router.dom.navLink

interface AnyListProps<O> : RProps {
    var objs: Array<O>
    var del: (Int) -> (Event) -> Unit
    var visible:Boolean
}

fun <T> fAnyList(name: String, path: String) =
    functionalComponent<AnyListProps<T>> {props ->
        h2 { +name }
        ol {
            attrs.start = "i"
            props.objs.mapIndexed{ index, obj ->
                li {
                    navLink("$path/$index"){
                        span{
                            attrs.id = "SP|$index"
                            +obj.toString();
                        }
                    }
                    if(props.visible){
                        button {
                            +"del"
                            attrs.onClickFunction = props.del(index)
                        }
                    }
                }
            }
        }
    }

fun <T> RBuilder.anyList(
    anys: Array<T>,
    name: String,
    path: String,
    del: (Int) -> (Event) -> Unit,
    visible:Boolean = true
) = child(withDisplayName(name, fAnyList<T>(name, path))){
    attrs.objs = anys
    attrs.del = del
    attrs.visible = visible
}