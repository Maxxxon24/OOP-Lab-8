package data

data class Lesson(
    var name: String
){
    override fun toString(): String = name
}

var lessonsList = arrayOf(
    Lesson("Lecture"),
    Lesson("Practice"),
    Lesson("Exam")
)