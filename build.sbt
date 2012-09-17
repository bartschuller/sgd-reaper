import ScalaxbKeys._

name := "sgd-reaper"

organization := "org.smop"

version := "0.2-SNAPSHOT"

scalaVersion := "2.9.2"

libraryDependencies ++= Seq(
  "org.smop" %% "oai-reaper" % "0.2-SNAPSHOT",
  "net.databinder" %% "dispatch-http" % "0.8.8",
  "org.mongodb" %% "casbah-gridfs" % "2.4.1",
  "org.scala-tools.testing" % "specs_2.9.1" % "1.6.9" % "test"
)

seq(scalaxbSettings: _*)
