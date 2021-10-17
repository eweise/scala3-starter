package eweise

import io.circe.Encoder
import scalikejdbc.config.DBs
import io.circe.*
import io.circe.generic.semiauto.deriveEncoder

import java.time.LocalDate
import io.circe.syntax.*
import io.circe.parser.decode
import io.circe.*
import io.circe.generic.semiauto.*
import scalikejdbc.DB


object MinimalApplication extends cask.MainRoutes {
  implicit val todoEncoder: Encoder[TodoRequest] = deriveEncoder[TodoRequest]
  implicit val todoDecoder: Decoder[TodoRequest] = deriveDecoder[TodoRequest]

  DBs.setupAll()

  val todoRepo = TodoRepository()

  todoRepo.initialize()

  @cask.get("/")
  def hello() = "Hello World!"

  @cask.post("/todo")
  def postTodo(request: cask.Request) = {
    val text = request.text()
    decode[TodoRequest](text) match
      case Left(failure) =>
        println(failure)
      case Right(todoRequest) =>
        DB localTx { implicit session =>
          todoRepo.createTodo(todoRequest.toModel())
        }
  }

  override def port: Int = 8082

  initialize()
}

