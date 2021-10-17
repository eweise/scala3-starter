package eweise

import java.time.LocalDate
import java.time.format.DateTimeFormatter._

case class TodoRequest(
                        title: String,
                        description: Option[String] = None,
                        dueDate: Option[LocalDate] = None
                      ) {

  def toModel(): Todo = new Todo(
    title = this.title,
    description = this.description,
    dueDate = this.dueDate
  )
}

case class TodoResponse(
                         id: String,
                         title: String,
                         description: Option[String] = None,
                         dueDate: Option[String] = None,
                         createdDate: String
                       )

object TodoResponse {
  def fromTodo(todo: Todo): TodoResponse = new TodoResponse(
    id = todo.id.get.toString,
    title = todo.title,
    description = todo.description,
    dueDate = todo.dueDate.map(iso),
    createdDate = todo.createdDate.iso
  )
}

extension (ld: LocalDate)
  def iso = ld.format(ISO_DATE)
