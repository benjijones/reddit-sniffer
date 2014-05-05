import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.routing.RoundRobinRouter
import kuhn.api.shutdown

object Test extends App {
	
	val reddits = List("pics", "funny", "gifs", "AskReddit")
	
	val system = ActorSystem("ActorSystem")
	val router = system.actorOf(Props[SubredditWalker].withRouter(RoundRobinRouter(4)), name = "linkRouter")
	
	reddits map WalkSubreddit foreach (router !)
	
	println("SLEEP...")
	Thread.sleep(180000)
	shutdown
	
	
	
	println(LinkMap toDot)
}
