package eweise

import org.scalatest.funsuite.AnyFunSuite
import scalikejdbc.config.DBs
import scalikejdbc.*
import org.scalatest.*
import matchers.should.*
import matchers.should.Matchers.*

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

  test("delete Todo") {
    withFixture {
      DB localTx { implicit session =>
        val newTodo = Todo(title = "hello", description = Some("world"))
        val repo = TodoRepository()
        repo.createTodo(newTodo)
        repo.deleteTodo(newTodo.id)
        val result = repo.findAllTodos
        result.map { found => found.id should not be newTodo.id }
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
