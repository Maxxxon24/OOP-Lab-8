package component

import data.*
import hoc.withDisplayName
import org.w3c.dom.*
import org.w3c.dom.events.Event
import react.*
import react.dom.*
import react.router.dom.*
import kotlin.browser.document

typealias EFT = (Event) -> Unit

interface AppProps : RProps {}

interface AppState : RState {
    var presents: Array<Array<Boolean>>
    var lessons: Array<Lesson>
    var students: Array<Student>
}

interface RouteNumberResult : RProps {
    var number: String
}

class App : RComponent<AppProps, AppState>() {
    override fun componentWillMount() {
        state.students = studentList
        state.lessons = lessonsList
        state.presents = Array(state.lessons.size) {
            Array(state.students.size) { false }
        }
    }

    override fun RBuilder.render() {

        header {
            h1 { +"App" }
            nav {
                ul {
                    li { navLink("/lessons") { +"Lessons" } }
                    li { navLink("/students") { +"Students" } }
                    li { navLink("/editlessons") { +"Edit lessons" } }
                    li { navLink("/editstudent") { +"Edit student" } }
                }
            }
        }

        switch {

            route("/editstudent") { editStudentPage() }
            route("/editlessons") { editLessonPage() }

            route("/lessons",
                exact = true,
                render = {
                    anyList(state.lessons, "Lessons", "/lessons")
                }
            )

            route("/students",
                exact = true,
                render = {
                    anyList(state.students, "Students", "/students")
                }
            )

            route("/lessons/:number",
                render = { route_props: RouteResultProps<RouteNumberResult> ->
                    val num = route_props.match.params.number.toIntOrNull() ?: -1
                    val lesson = state.lessons.getOrNull(num)
                    if (lesson != null)
                        anyFull(
                            RBuilder::student,
                            lesson,
                            state.students,
                            state.presents[num]
                        ) { onClick(num, it) }
                    else
                        p { +"No such lesson" }
                }
            )

            route("/students/:number",
                render = { route_props: RouteResultProps<RouteNumberResult> ->
                    val num = route_props.match.params.number.toIntOrNull() ?: -1
                    val student = state.students.getOrNull(num)
                    if (student != null)
                        anyFull(
                            RBuilder::lesson,
                            student,
                            state.lessons,
                            state.presents.map {
                                it[num]
                            }.toTypedArray()
                        ) { onClick(it, num) }
                    else
                        p { +"No such student" }
                }
            )

        }
    }

    private fun RElementBuilder<RProps>.editStudentPage(): ReactElement {
        return anyListRedact(
            "Students",
            "/students",
            RBuilder::editPage,
            RBuilder::anyList,
            state.students,
            removeStudent(),
            editStudent(),
            newStudent()
        )
    }

    private fun RElementBuilder<RProps>.editLessonPage(): ReactElement {
        return anyListRedact(
            "Lessons",
            "/lessons",
            RBuilder::editPage,
            RBuilder::anyList,
            state.lessons,
            removeLesson(),
            editLesson(),
            newLesson()
        )
    }

    private fun newStudent(): EFT = {
        val newStudent = getInputValue("studentsAdd")
        setState { students += Student(getName(newStudent), getSName(newStudent)) }
    }

    private fun newLesson(): EFT = {
        setState {
	        lessons += Lesson( getInputValue("lessonsAdd") )
	        presents += arrayOf( Array(state.students.size ) { false })
        }
    }

    private fun removeStudent(): EFT = {
        setState {
            students = students.toMutableList().apply { removeAt(getInputIndex("studentsDelete")) }.toTypedArray()
        }
    }

    private fun removeLesson(): EFT = {
        setState {
            lessons = lessons.toMutableList().apply { removeAt(getInputIndex("lessonsDelete")) }.toTypedArray()
        }
    }

    private fun editStudent(): EFT = {
        val editStudentIndex = getInputIndex("Editstudents")
        val newStudent = getInputValue("studentsFull")
        setState { students[editStudentIndex] = Student(getName(newStudent), getSName(newStudent)) }
    }

    private fun editLesson(): EFT = {
        val editLessonIndex = getInputIndex("Editlessons")
        val newLesson = getInputValue("lessonsFull")
        setState { lessons[editLessonIndex] = Lesson(newLesson) }
    }

    private fun onClick(indexLesson: Int, indexStudent: Int) = { _: Event ->
        setState {
            presents[indexLesson][indexStudent] = !presents[indexLesson][indexStudent]
        }
    }

    private fun getName(str:String):String = str.substringBefore(" ")
    private fun getSName(str:String):String = str.substringAfter(" ")
    private fun getInputValue(str:String):String = (document.getElementById(str) as HTMLInputElement).value
    private fun getInputIndex(str:String):Int = getInputValue(str).toInt()
}

fun RBuilder.app() = child(withDisplayName("AppHoc", App::class)) {}