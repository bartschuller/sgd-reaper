import ScalaxbKeys._

name := "sgd-reaper"

organization := "org.smop"

version := "0.2-SNAPSHOT"

scalaVersion := "2.9.1"

libraryDependencies ++= Seq(
  "org.smop" %% "oai-reaper" % "0.2-SNAPSHOT",
  "net.databinder" %% "dispatch-http" % "0.7.8",
  "com.mongodb.casbah" %% "casbah" % "2.1.5-1",
  "org.scala-tools.testing" %% "specs" % "1.6.9" % "test"
)

seq(scalaxbSettings: _*)
