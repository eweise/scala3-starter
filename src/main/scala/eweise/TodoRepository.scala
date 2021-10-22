package eweise

import scalikejdbc.*

import java.time.LocalTime
import java.util.UUID

class TodoRepository {
  val t = (TodoSupport.syntax("t"))
  val c = TodoSupport.column

  def createTodo(todo: Todo)(using s: DBSession = AutoSession): Unit = {
    val id =
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
  }

  def findAllTodos(using s: DBSession = AutoSession): List[Todo] = {
    sql"select ${t.result.*} from ${TodoSupport.as(t)} where is_complete = false".map(TodoSupport(t)).list.apply()
  }

  def complete(id: UUID, isComplete: Boolean)(using s: DBSession = AutoSession): Either[InternalException | NotFoundException, Unit] =
    handlingException {
      val updateCount = sql"update ${TodoSupport.as(t)} set is_complete = ${isComplete} where ${c.id} = ${id}".update.apply()
      println("update count = " + updateCount)
      val result = if updateCount != 1 then
        Left(new NotFoundException("todo", id))
      else
        Right(())
      result
    }

  def handlingException[A <: Exception, B](f: Either[A, B]): Either[InternalException | A, B] =
    try
      f
    catch
      case e: Exception => Left(new InternalException())
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