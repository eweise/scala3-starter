import java.time.LocalDate

case class Todo(
                 title: String,
                 description: Option[String],
                 duDate: Option[LocalDate],
                 createdDate: LocalDate,
               )