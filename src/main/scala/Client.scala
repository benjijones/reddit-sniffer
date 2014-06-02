import akka.actor.ActorSystem
import akka.actor.actorRef2Scala
import kuhn.api.first_link
import kuhn.api.frontpage
import nodes.Backend
import nodes.Frontend
import walker.SearchSubreddit

object Client extends App {
	
	kuhn.Console.main(Array[String]())
	
	Backend.main(Array[String]())
	Frontend.main(Array[String]())
	
	val system = ActorSystem("ClusterSystem")
	
	val reddits = List("pics", "funny", "gifs", "AskReddit")
	
	reddits foreach (Frontend.frontend ! SearchSubreddit(_))
	
	println("SLEEP...")
	Thread.sleep(20000)
}