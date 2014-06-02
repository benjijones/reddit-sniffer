package nodes

import scala.annotation.varargs
import com.typesafe.config.ConfigFactory
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorSystem
import akka.actor.Props
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.InitialStateAsEvents
import akka.cluster.ClusterEvent.MemberUp
import akka.cluster.ClusterEvent.UnreachableMember
import akka.actor.Address
import akka.cluster.ClusterEvent.CurrentClusterState
import akka.cluster.MemberStatus
import akka.actor.RootActorPath
import walker.RegisterLinkWorker

object Backend {
	def main(args : Array[String]) {
		if (args.isEmpty)
			startup(Seq("8989", "8990", "0"))
		else
			startup(args)
	}
	
	def startup(ports : Seq[String]) {
		ports foreach { port =>
			val config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port)
				.withFallback(ConfigFactory.parseString("akka.cluster.roles = [backend]"))
				.withFallback(ConfigFactory.load())
			val system = ActorSystem("ClusterSystem", config)
			
			system.actorOf(Props[Backend], name = "backendWorker")
		}
	}	
}

class Backend extends Actor with ActorLogging {
	
//	val redditURLPattern = new Regex(""".*(http\://)?(www\.|np\.)reddit\.com/r/([a-zA-Z0-9]+)/comments/([a-zA-Z0-9]+)/[a-zA-Z0-9_]+/([a-zA-Z0-9]+).*""", "http", "www", "subreddit", "linkId", "commentId")
	
	val cluster = Cluster(context.system)
	
	override def preStart() : Unit = cluster.subscribe(self, initialStateMode = InitialStateAsEvents,
										classOf[MemberUp], classOf[UnreachableMember])
	override def postStop() : Unit = cluster.unsubscribe(self)
	
	def receive = {
		case MemberUp(member) if member.hasRole("frontend") =>
			context.actorSelection(RootActorPath(member.address) / "user" / "frontend") ! RegisterLinkWorker(this.self)
//			log.info(this + " sees " + member.address)
		case UnreachableMember(member) =>
			log.info("Member detected as unreachable: {}", member)
//			x ! Identify(None)
//			
//			x ! RegisterLinkWorker(this.self)

		case state: CurrentClusterState =>
			state.members.filter(_.status == MemberStatus.Up) foreach { member =>
				context.actorSelection(RootActorPath(member.address) / "user" / "frontend") ! RegisterLinkWorker(this.self)
			}
//		case WalkLink(linkId) => 
//			val replyTo = sender
//			println(this.context.self + " received " + linkId + ", replyTo: " + replyTo)
//			comments(linkId) {
//				case (ancestors, comment) => 
//					walk(comment, replyTo)
//			}
	}
//	
//	def walk(comment : Comment, replyTo : ActorRef) {
//		print(this + " is reading " + comment.id + "...")
//		comment.body match {
//			case redditURLPattern(http, www, subreddit, linkId, commentId) => 
////				println("MATCH FOUND.")
//				replyTo ! new AddToLinkMap(CommentLocation(comment), CommentLocation(subreddit, linkId, commentId))
//			case _ =>
////				println("not found.")
//		}
//	}
}
