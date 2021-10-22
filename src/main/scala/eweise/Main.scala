package eweise

import eweise.TodoResponse.fromTodo
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

import java.util.UUID


object MinimalApplication extends cask.MainRoutes {
  implicit val todoEncoder: Encoder[TodoResponse] = deriveEncoder[TodoResponse]
  implicit val todoDecoder: Decoder[TodoRequest] = deriveDecoder[TodoRequest]

  override def port: Int = 8082

  import com.typesafe.config.Config
  import com.typesafe.config.ConfigFactory

  val conf: Config = ConfigFactory.load()

  DBs.setupAll()

  new Migrator(conf)

  val todoRepo = TodoRepository()

  @cask.get("/")
  def ping() =
    if 1 == 1 then
      "todos"
    else
      cask.Abort(404)

  @cask.get("/todos")
  def findAllTodos() =
    val responses = todoRepo.findAllTodos.map(fromTodo)
    responses.asJson.toString()

  @cask.post("/todo")
  def postTodo(request: cask.Request) =
    val text = request.text()
    decode[TodoRequest](text) match
      case Left(failure) =>
        println(failure)
      case Right(todoRequest) =>
        DB localTx { implicit session =>
          todoRepo.createTodo(todoRequest.toModel())
        }

  @cask.put("/todo/:id/complete")
  def complete(id: String) =
    val uuid = UUID.fromString(id)
    val result = todoRepo.complete(uuid, true)
    result match
      case Left(ex) =>
        ex match
          case e: InternalException => cask.Abort(500)
          case e: NotFoundException => cask.Abort(404)
          throw ex
      case Right(()) => ""

  initialize()
}

