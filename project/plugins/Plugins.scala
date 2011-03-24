import sbt._

class Plugins(info: ProjectInfo) extends PluginDefinition(info) {
  val scalaxb = "org.scalaxb" % "sbt-scalaxb" % "0.5.4-SNAPSHOT"

  val scalaToolsNexusSnapshots = "Scala Tools Nexus Snapshots" at "http://nexus.scala-tools.org/content/repositories/snapshots/"
  val scalaToolsNexusReleases  = "Scala Tools Nexus Releases" at "http://nexus.scala-tools.org/content/repositories/releases/"
}
