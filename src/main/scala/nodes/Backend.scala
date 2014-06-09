package nodes

import scala.util.matching.Regex

import com.typesafe.config.ConfigFactory

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.actor.{ActorSystem, Props, RootActorPath, actorRef2Scala}
import akka.actor.ActorSelection.toScala
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.{CurrentClusterState, InitialStateAsEvents, MemberUp, UnreachableMember}
import akka.cluster.MemberStatus
import kuhn.Comment
import kuhn.api.comments
import linkmap.CommentLocation
import walker.{AddToLinkMap, RegisterLinkWorker, WalkLink}

object Backend {
	
	def main(args : Array[String]) {
		if (args.isEmpty)
			println("missing argument: must provide a port number")
		else {
			if (!actorSystems.exists(_.settings.config.getString("akka.remote.netty.tcp.port") == args(0))) {
				println("starting backend actor on port " + args(0))
				startup(args(0))
			} else 
				println("port already in use on this machine")
		}
	}
	
	var actorSystems = List[ActorSystem]()
	
	def startup(port : String) {
		val config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port)
			.withFallback(ConfigFactory.parseString("akka.cluster.roles = [backend]"))
			.withFallback(ConfigFactory.load())
			
		val system = ActorSystem("ClusterSystem", config)
			
		system.actorOf(Props[Backend])
			
		actorSystems = actorSystems :+ system
	}
	
	def shutdown {
		actorSystems foreach(_.shutdown)
	}
}

class Backend extends Actor with ActorLogging {
	
	val redditURLPattern = new Regex("""(?s).*(?:http\://)?(?:www\.|np\.)reddit\.com/r/(\w+)/comments/(\w+)/\w+/(\w+).*""", "subreddit", "linkId", "commentId")

	val cluster = Cluster(context.system)
	
	override def preStart() : Unit = cluster.subscribe(self, initialStateMode = InitialStateAsEvents,
										classOf[MemberUp], classOf[UnreachableMember])
	override def postStop() : Unit = cluster.unsubscribe(self)
	
	def receive = {
		case MemberUp(member) if member.hasRole("frontend") =>
			context.actorSelection(RootActorPath(member.address) / "user" / "frontend") ! RegisterLinkWorker(this.self)
		case UnreachableMember(member) =>
			log.info("Member detected as unreachable: {}", member)
		case state: CurrentClusterState =>
			state.members.filter(_.status == MemberStatus.Up) foreach { member =>
				context.actorSelection(RootActorPath(member.address) / "user" / "frontend") ! RegisterLinkWorker(this.self)
			}
			
		case WalkLink(linkId) => 
			val replyTo = sender
			comments(linkId) {
				case (ancestors, comment) => 
					walk(comment, replyTo)
			}
	}
	
	def walk(comment : Comment, replyTo : ActorRef) {
		comment.body match {
			case redditURLPattern(subreddit, linkId, commentId) => 
				replyTo ! AddToLinkMap(CommentLocation(comment), CommentLocation(subreddit, linkId, commentId))
			case _ => 
		}
	}
}
