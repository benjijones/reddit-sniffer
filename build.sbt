name := "reddit-sniffer"

organization := "jones"

resolvers := Seq("Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/")

libraryDependencies := Seq("com.typesafe.akka" %% "akka-actor" % "2.3.2",
			   "com.typesafe.akka" %% "akka-remote" % "2.3.2",
  			   "com.typesafe.akka" %% "akka-contrib" % "2.3.2",
  			   "com.typesafe.akka" %% "akka-cluster" % "2.3.2")
