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

  @cask.post("/do-thing")
  def doThing(request: cask.Request) = request.text().reverse

  override def port: Int = 8082

  initialize()
}

