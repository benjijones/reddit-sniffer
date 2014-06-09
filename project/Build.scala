import sbt._
import sbt.Keys._

object MultiBuild extends Build {
  lazy val root = project.in(file("."))
    .dependsOn(scalaReddit)
  lazy val scalaReddit = project in file("reddhead")
}
