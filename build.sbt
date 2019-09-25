name := "fuel-meter"
maintainer := "devproltd@perhac.com"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.8"
resolvers += Resolver.bintrayRepo("hmrc", "releases")

libraryDependencies ++= Seq(
  guice,
  ws,
  jdbc,
  "org.typelevel"  %% "cats-core"    % "2.0.0",
  "org.tpolecat"   %% "doobie-core"  % "0.5.3",
  "org.postgresql" % "postgresql"    % "42.2.8",
  "uk.gov.hmrc"    %% "uri-template" % "1.4.0"
  /* "org.xerial"    % "sqlite-jdbc"    % "3.28.0" */
)

excludeDependencies ++= Seq(
  ExclusionRule("com.typesafe.akka", "akka-protobuf-v3_2.12"),
  ExclusionRule("com.google.protobuf", "protobuf-java")
)

scalacOptions in ThisBuild ++= Seq("-unchecked", "-deprecation", "-feature")

sources in (Compile, doc) := Seq.empty
publishArtifact in (Compile, doc) := false
