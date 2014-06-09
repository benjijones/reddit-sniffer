package nodes

import com.typesafe.config.ConfigFactory

import akka.actor.{Actor, ActorRef, ActorSystem, Props, actorRef2Scala}
import kuhn.api.{links, subreddit}
import linkmap.LinkMap
import walker.{AddToLinkMap, RegisterLinkWorker, SearchSubreddit, WalkLink}

object Frontend extends App {	
	
	private val config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + 8988)
	 	.withFallback(ConfigFactory.parseString("akka.cluster.roles = [frontend]"))
		.withFallback(ConfigFactory.load())
	val system = ActorSystem("ClusterSystem", config)
	
	val frontend = system.actorOf(Props[Frontend], name = "frontend")
	
	def shutdown {
		system.shutdown
	}
}

class Frontend extends Actor {
	
	var backendWorkers = IndexedSeq[ActorRef]()
	var subredditWorkers = IndexedSeq[ActorRef]()
	var jobCount = 0
	
	def receive = {	
		
		case SearchSubreddit(name) =>
			val subredditWorker = context.actorOf(Props(classOf[SubredditWorker], name), name)
			backendWorkers foreach {subredditWorker ! RegisterLinkWorker(_)}
			subredditWorkers = subredditWorkers :+ subredditWorker
		
		case rlw : RegisterLinkWorker if !backendWorkers.contains(sender) => 
			backendWorkers = backendWorkers :+ sender
			subredditWorkers foreach {_ forward rlw}
		
		case AddToLinkMap(source, destination) =>
			println("adding to linkMap: " + source + " -> " + destination)
			LinkMap(source) = destination
	}
}
