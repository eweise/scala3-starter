package eweise

import java.time.LocalDate

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


