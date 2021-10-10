package eweise

import org.scalatest.funsuite.AnyFunSuite
import scalikejdbc.config.DBs
import scalikejdbc._
import org.scalatest._
import matchers.should._
import matchers.should.Matchers._

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

  test("delete Todo") {
    withFixture {
      DB localTx { implicit session =>
        val newTodo = Todo(title = "hello", description = Some("world"))
        val repo = TodoRepository()
        repo.createTodo(newTodo)
        repo.deleteTodo(newTodo.id)
        repo.findAllTodos.map {found => found.id should not be newTodo.id}
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
}
