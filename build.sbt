name := """fuel-meter"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

routesGenerator := InjectedRoutesGenerator
libraryDependencies ++= Seq(
  "org.reactivemongo" %% "play2-reactivemongo" % "0.11.7.play24"
)

libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-compiler" % _ )

JsEngineKeys.engineType := JsEngineKeys.EngineType.Node
