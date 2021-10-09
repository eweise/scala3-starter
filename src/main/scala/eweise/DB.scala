package eweise

import scalikejdbc._

object DB {
  def initConnectionPool(url: String, user: String, password: String): Unit =
    // after loading JDBC drivers
    ConnectionPool.singleton(url, user, password)
    ConnectionPool.add("postgres-todos", url, user, password)

    val settings = ConnectionPoolSettings(
      initialSize = 5,
      maxSize = 20,
      connectionTimeoutMillis = 3000L,
      validationQuery = "select 1 from dual")

    ConnectionPool.add("postgres-todos", url, user, password, settings)
    
    
}

