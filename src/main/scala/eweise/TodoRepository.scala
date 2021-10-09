package eweise

import scalikejdbc.*

import java.time.LocalTime
import java.util.UUID

class TodoRepository {

  def createTodo(todo: Todo)(using s: DBSession = AutoSession): Unit = {
    val c = TodoSupport.column

    sql"""insert into todo (
         ${c.id},
         ${c.title},
         ${c.description},
         ${c.dueDate}, ${c.createdDate})
         values (
         uuid(${todo.id.toString}),
         ${todo.title},
         ${todo.description},
         ${todo.dueDate},
         ${todo.createdDate})""".update.apply()
  }

  def findAllTodos(using s: DBSession = AutoSession):List[Todo] = {
    val t = (TodoSupport.syntax("t"))
    sql"select ${t.result.*} from ${TodoSupport.as(t)}".map(TodoSupport(t)).list.apply()
  }

  def initialize(implicit s: DBSession = AutoSession): Unit = {
    val c = TodoSupport.column
    sql"""create table if not exists todo (
         ${c.id} uuid not null,
         ${c.title} text not null,
         ${c.description} text,
         ${c.dueDate} timestamp without time zone,
         ${c.createdDate} timestamp without time zone not null,
          CONSTRAINT todo_pkey PRIMARY KEY (id))
                    """.update.apply()
  }
}

object TodoSupport extends SQLSyntaxSupport[Todo] {
  override val tableName = "todo"

  def apply(o: SyntaxProvider[Todo])(rs: WrappedResultSet): Todo =
    apply(o.resultName)(rs)

  def apply(g: ResultName[Todo])(rs: WrappedResultSet) = eweise.Todo(
    id = UUID.fromString(rs.string(g.id)),
    title = rs.string(g.title),
    description = rs.stringOpt(g.description),
    dueDate = rs.localDateOpt(g.dueDate),
    createdDate = rs.localDate(g.createdDate),
  )
}