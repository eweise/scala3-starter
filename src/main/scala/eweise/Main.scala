package eweise

import scalikejdbc.config.DBs

object MinimalApplication extends cask.MainRoutes {

  DBs.setupAll()

  val todoRepo = TodoRepository()

  todoRepo.initialize()

  @cask.get("/")
  def hello() = "Hello World!"

  @cask.post("/do-thing")
  def doThing(request: cask.Request) = request.text().reverse

  override def port: Int = 8082

  initialize()
}

