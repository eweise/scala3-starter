package eweise

import scalikejdbc.*

import java.time.LocalTime
import java.util.UUID

class TodoRepository {
  val t = (TodoSupport.syntax("t"))
  val c = TodoSupport.column

  def createTodo(todo: Todo)(using s: DBSession = AutoSession): Unit =
    sql"""insert into todo (
         ${c.id},
         ${c.title},
         ${c.description},
         ${c.dueDate}, ${c.createdDate})
         values (
         uuid(${todo.id.get.toString}),
         ${todo.title},
         ${todo.description},
         ${todo.dueDate},
         ${todo.createdDate})""".update.apply()

  def findAllTodos(using s: DBSession = AutoSession): List[Todo] =
    sql"select ${t.result.*} from ${TodoSupport.as(t)} where is_complete = false".map(TodoSupport(t)).list.apply()

  def getTodo(id: UUID)(using s: DBSession = AutoSession): Option[Todo] =
    val result = sql"select ${t.result.*} from ${TodoSupport.as(t)} where ${c.id} = ${id}".map(TodoSupport(t)).list.apply()
    None


  def complete(id: UUID, isComplete: Boolean)(using s: DBSession = AutoSession): Either[NotFoundException, Unit] =
    val updateCount = sql"update ${TodoSupport.as(t)} set is_complete = ${isComplete} where ${c.id} = ${id}".update.apply()
    if updateCount != 1 then
      Left(new NotFoundException("todo", id))
    else
      Right(())

}

object TodoSupport extends SQLSyntaxSupport[Todo] {
  override val tableName = "todo"

  def apply(o: SyntaxProvider[Todo])(rs: WrappedResultSet): Todo = apply(o.resultName)(rs)

  def apply(g: ResultName[Todo])(rs: WrappedResultSet) = eweise.Todo(
    id = Some(UUID.fromString(rs.string(g.id))),
    title = rs.string(g.title),
    description = rs.stringOpt(g.description),
    dueDate = rs.localDateOpt(g.dueDate),
    createdDate = rs.localDate(g.createdDate),
  )
}