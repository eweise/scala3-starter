val dottyVersion = "3.0.2"

lazy val root = project
  .in(file("."))
  .settings(
    name := "todos",
    version := "0.1.0",

    scalaVersion := "3.0.2",

    libraryDependencies ++= Seq(
      "com.lihaoyi" %% "cask" % "0.7.12",
      "org.postgresql" % "postgresql" % "42.2.24",
      "org.scalikejdbc" %% "scalikejdbc" % "4.0.0-RC2",
      "org.scalikejdbc" %% "scalikejdbc-config"  % "4.0.0-RC2",
      "com.typesafe" % "config" % "1.4.1",
      "ch.qos.logback"  %  "logback-classic"   % "1.2.6",
      // ScalaTest has already 3.0 implementation
      "org.scalactic" %% "scalactic" % "3.2.9",
      "org.scalatest" %% "scalatest" % "3.2.9" % "test",
      "org.scalatest" %% "scalatest-flatspec" % "3.2.9" % "test"
    )
  )
