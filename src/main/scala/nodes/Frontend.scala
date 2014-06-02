package nodes

import scala.annotation.varargs
import com.typesafe.config.ConfigFactory
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.InitialStateAsEvents
import akka.cluster.ClusterEvent.MemberUp
import linkmap.LinkMap
import walker.AddToLinkMap
import walker.RegisterLinkWorker
import walker.SearchSubreddit
import walker.SubredditWorker
import akka.actor.Address
import akka.actor.ActorLogging

object Frontend extends App {	
	
	val config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + 8988)
	 	.withFallback(ConfigFactory.parseString("akka.cluster.roles = [frontend]"))
        .withFallback(ConfigFactory.load())
	val system = ActorSystem("ClusterSystem", config)
	
	val frontend = system.actorOf(Props[Frontend], name = "frontend")
//	
//	println("SLEEP...")
//	Thread.sleep(60000)
//	
//	println(LinkMap toDot)
}

class Frontend extends Actor with ActorLogging {
	
	val subredditWorker = context.actorOf(Props[SubredditWorker])
	
	def receive = {		
		case ss : SearchSubreddit => println("WOW! WE HAVE SOMETHING!") ; subredditWorker forward ss
		
		case rlw : RegisterLinkWorker => subredditWorker forward rlw
		
		case AddToLinkMap(source, destination) => LinkMap(source) = destination
	}
	
	
}