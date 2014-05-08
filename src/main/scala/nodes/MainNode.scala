package nodes

import com.typesafe.config.ConfigFactory

import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.routing.RoundRobinRouter
import kuhn.api.shutdown
import linkmap.LinkMap
import walker.LinkWalker
import walker.SubredditWalker
import walker.WalkSubreddit

object MainNode extends App {	
	
	val reddits = List("pics", "funny", "gifs", "AskReddit")
	
	val system = ActorSystem("MainSystem", ConfigFactory.load.getConfig("MainSystem"))
	val subredditActor = system.actorOf(Props[SubredditWalker], name = "subredditActor")
	val linkRouter = system.actorOf(Props[LinkWalker].withRouter(RoundRobinRouter(2)), name = "linkRouter") 
	
	reddits map (subreddit => WalkSubreddit(subreddit, linkRouter)) foreach (subredditActor !)
	
	println("SLEEP...")
	Thread.sleep(60000)
	
	system.shutdown
	shutdown
	
	println(LinkMap toDot)
}
