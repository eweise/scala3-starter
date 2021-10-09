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
