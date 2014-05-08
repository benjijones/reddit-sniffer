import sbt._


object MyBuild extends Build {

  lazy val root = Project("reddit-sniffer", file("."))
//                    .dependsOn(redditProject)
		  .dependsOn(reddheadProject)

  //lazy val redditProject = RootProject(uri("git://github.com/benjijones/reddit-sniffer.git"))
  lazy val reddheadProject = RootProject(file("/workspace/reddhead"))
  
}
