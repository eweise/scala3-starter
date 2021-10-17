package eweise

import java.time.LocalDate
import java.util.UUID

case class Todo(
                 id: Option[UUID] = Some(UUID.randomUUID()),
                 title: String,
                 description: Option[String] = None,
                 dueDate: Option[LocalDate] = None,
                 createdDate: LocalDate = LocalDate.now(),
               )