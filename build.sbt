import play._

name := "retrofit-svc-api-test"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  javaWs,
  "pl.matisoft" %% "play-hysterix" % "0.2.10",
  "com.netflix.ribbon" % "ribbon-core" % "2.0-RC5",
  "com.netflix.ribbon" % "ribbon" % "2.0-RC5",
  "com.netflix.ribbon" % "ribbon-transport" % "2.0-RC5",
  "com.netflix.rxjava" % "rxjava-core" % "0.20.0-RC4",
  "com.squareup.retrofit" % "retrofit" % "1.6.1"
)
