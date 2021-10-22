package eweise

import com.typesafe.config.ConfigException.ValidationFailed

import java.time.LocalDate
import java.util.UUID


class ValidationException(msg: String) extends RuntimeException(msg)
class NotFoundException(tag:String, id:UUID) extends RuntimeException(s"$tag with ID: $id not found")
class InternalException extends RuntimeException()

case class Todo(
                 id: Option[UUID] = Some(UUID.randomUUID()),
                 title: String,
                 description: Option[String] = None,
                 dueDate: Option[LocalDate] = None,
                 createdDate: LocalDate = LocalDate.now(),
                 completed: Boolean = false
               )


object Todo {
  def create(
              title: String,
              description: Option[String] = None,
              dueDate: Option[LocalDate] = None,
            ): Either[ValidationException, Todo] =
    new Right(Todo(title = title, description = description, dueDate = dueDate))
}