package eweise

import java.time.LocalDate
import java.util.UUID

case class Todo(
                 id : UUID = UUID.randomUUID(),
                 title: String,
                 description: Option[String],
                 dueDate: Option[LocalDate] = Option.empty,
                 createdDate: LocalDate = LocalDate.now(),
               )