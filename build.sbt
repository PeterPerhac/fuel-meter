name := "fuel-meter"
maintainer := "devproltd@perhac.com"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.8"

resolvers += "hmrc-releases" at "https://artefacts.tax.service.gov.uk/artifactory/hmrc-releases/"

libraryDependencies ++= Seq(
  jdbc,
  "org.typelevel"  %% "cats-core"    % "2.0.0",
  "org.tpolecat"   %% "doobie-core"  % "0.8.2",
  "org.postgresql" % "postgresql"    % "42.2.8",
  "org.scalaj"     %% "scalaj-http"  % "2.4.2",
  "uk.gov.hmrc"    %% "uri-template" % "1.8.0"
)

excludeDependencies ++= Seq(
  ExclusionRule("com.typesafe.akka", "akka-protobuf-v3_2.12"),
  ExclusionRule("com.google.protobuf", "protobuf-java")
)

scalacOptions in ThisBuild ++= Seq("-unchecked", "-deprecation", "-feature")
addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full)

sources in (Compile, doc) := Seq.empty
publishArtifact in (Compile, doc) := false
