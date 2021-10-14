package eweise

import scalikejdbc.config.DBs

import java.time.LocalDate

object MinimalApplication extends cask.MainRoutes {

  DBs.setupAll()

  val todoRepo = TodoRepository()

  todoRepo.initialize()

  //  implicit val foo: java.time.LocalDate = upickle.default.macroRW[java.time.LocalDate]
  //  upickle.default.macroRW[Todo]

  @cask.get("/")
  def hello() = "Hello World!"

  @cask.post("/todo")
  def doThing(request: cask.Request) = {
    val one = "world"
    val text = request.text()
    val json = ujson.read(text)
    val todo = Todo(
      title = json("title").str,
      description = Some(json("description").str)
    )
  }

  override def port: Int = 8082

  initialize()
}

