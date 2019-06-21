name := "fuel-meter"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.8"

resolvers += Resolver.bintrayRepo("hmrc", "releases")

libraryDependencies ++= Seq(
  guice,
  ws,
  "org.typelevel"     %% "cats-core"                     % "1.6.1",
  "org.typelevel"     %% "mouse"                         % "0.19",
  "org.reactivemongo" %% "reactivemongo"                 % "0.17.1",
  "org.reactivemongo" %% "play2-reactivemongo"           % "0.17.1-play26",
  "uk.gov.hmrc"       %% "play-conditional-form-mapping" % "1.1.0-play-26"
)

libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-compiler" % _)

scalacOptions in ThisBuild ++= Seq("-unchecked", "-deprecation", "-feature")
