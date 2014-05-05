import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.routing.RoundRobinRouter
import kuhn.Link
import kuhn.api.links
import kuhn.api.subreddit

class SubredditWalker extends RedditWalkable {
	
	val router = context.actorOf(Props[LinkWalker].withRouter(RoundRobinRouter(20)), name = "linkRouter")
	
	def receive = {
		case WalkSubreddit(name) => links(subreddit(name)) { case link => walk(link) }
	}
	
	private def walk(link : Link) {
		router ! new WalkLink(link.id)
	}
	
}