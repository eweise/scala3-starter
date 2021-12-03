package eweise
import com.typesafe.config.Config
import org.flywaydb.core.Flyway

class Migrator(config: Config)  {

  val flyway = Flyway
    .configure()
    .dataSource(
      config.getString("db.default.url"),
      config.getString("db.default.user"),
      config.getString("db.default.password")
    ).load()

  flyway.migrate()

}
