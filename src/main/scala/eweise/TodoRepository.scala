package eweise

import scalikejdbc.*

import java.time.LocalTime
import java.util.UUID

class TodoRepository {

  def createTodo(todo: Todo)(using s: DBSession = AutoSession): Unit = {
    val c = TodoSupport.column
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
    val t = (TodoSupport.syntax("t"))
    sql"select ${t.result.*} from ${TodoSupport.as(t)} where is_complete = false".map(TodoSupport(t)).list.apply()
  }

  def complete(id: UUID, isComplete: Boolean)(using s: DBSession = AutoSession): Unit = {
    val t = (TodoSupport.syntax("t"))
    val c = TodoSupport.column
    val nbrDeleted = sql"update ${TodoSupport.as(t)} set is_complete = ${isComplete} where ${c.id} = ${id}".update.apply()
    if nbrDeleted != 1 then
      throw new Exception(s"delete not success for todo.id=${id}")
  }

  def initialize(): Unit = {
    DB localTx { implicit session =>
      sql"""create table if not exists todo (
         id uuid not null,
         title text not null,
         description text,
         due_date timestamp without time zone,
         is_complete bool default false,
         created_date timestamp without time zone not null,
          CONSTRAINT todo_pkey PRIMARY KEY (id))
                    """.update.apply()
    }
  }
}

object TodoSupport extends SQLSyntaxSupport[Todo] {
  override val tableName = "todo"

  def apply(o: SyntaxProvider[Todo])(rs: WrappedResultSet): Todo =
    apply(o.resultName)(rs)

  def apply(g: ResultName[Todo])(rs: WrappedResultSet) = eweise.Todo(
    id = Some(UUID.fromString(rs.string(g.id))),
    title = rs.string(g.title),
    description = rs.stringOpt(g.description),
    dueDate = rs.localDateOpt(g.dueDate),
    createdDate = rs.localDate(g.createdDate),
  )
}