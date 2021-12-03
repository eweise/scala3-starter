package eweise

import eweise.MinimalApplication.todoRepo
import org.scalatest.funsuite.AnyFunSuite
import scalikejdbc.config.DBs
import scalikejdbc.*
import org.scalatest.*
import matchers.should.*
import matchers.should.Matchers.*

import java.util.UUID
import scala.Right

class TodoRepositoryTestSuite extends AnyFunSuite {
  def withFixture(f: => Unit): Unit = {
    DBs.setupAll()
    f
  }

  test("insert Todos") {
    withFixture {
      DB localTx { implicit session =>
        val newTodo = Todo(title = "hello", description = Some("world"))
        TodoRepository().createTodo(newTodo)
      }
    }
  }

  test("complete Todo") {
    withFixture {
      DB localTx { implicit session =>
        val newTodo = Todo(title = "hello", description = Some("world"))
        val repo = TodoRepository()
        repo.createTodo(newTodo)
        repo.complete(newTodo.id.get, true)
        val result = repo.findAllTodos
        result.map { found =>
          found.id should not be newTodo.id
        }
      }
    }
  }

  test("retrieve all todos") {
    withFixture {
      DB localTx { implicit session =>
        val newTodo = Todo(title = "hello", description = Some("world"))
        val repo = TodoRepository()
        repo.createTodo(newTodo)
        repo.findAllTodos.size should be > 0
      }
    }
  }
  test("retrieve a todo") {
    withFixture {
      DB localTx { implicit session =>
        val repo = TodoRepository()
        val result = repo.getTodo(UUID.randomUUID())
        assert(result == None)

        val newTodo = Todo(title = "hello", description = Some("world"))
        repo.createTodo(newTodo)
        val result2 = repo.getTodo(newTodo.id.get)
        assert( result2 == Some(newTodo))
      }
    }
  }

}
